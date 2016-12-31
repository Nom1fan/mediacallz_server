package com.mediacallz.server.controllers;

import com.google.gson.Gson;
import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.*;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.MediaFileDBO;
import com.mediacallz.server.database.dbo.MediaTransferDBO;
import com.mediacallz.server.handlers.upload_controller.SpMediaPathHandler;
import com.mediacallz.server.model.request.UploadFileRequest;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.utils.MediaFilesUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 12/09/2016.
 */
@Controller
public class UploadController extends AbstractController {

    private final UsersDataAccess usersDataAccess;

    private final PushSender pushSender;

    private final Dao dao;

    private final Gson gson;

    private final Map<SpecialMediaType, SpMediaPathHandler> spMedia2PathHandlerMap = new HashMap<>();

    @Autowired
    public UploadController(UsersDataAccess usersDataAccess, PushSender pushSender, Dao dao, Gson gson) {
        this.usersDataAccess = usersDataAccess;
        this.pushSender = pushSender;
        this.dao = dao;
        this.gson = gson;
    }

    @Autowired
    public void initMap(List<SpMediaPathHandler> spMediaPathHandlerList) {
        for (SpMediaPathHandler spMediaPathHandler : spMediaPathHandlerList) {
            spMedia2PathHandlerMap.put(spMediaPathHandler.getHandledSpMediaType(), spMediaPathHandler);
        }
    }


    @RequestMapping(value = "/v1/UploadFile", method = RequestMethod.POST)
    public void uploadFile(
            @RequestParam("fileForUpload") @Valid @NotNull MultipartFile fileForUpload,
            @RequestParam("jsonPart") @Valid @NotNull @NotBlank String requestString,
            HttpServletResponse response) throws IOException, ServletException {

        UploadFileRequest request = gson.fromJson(requestString, UploadFileRequest.class);
        StringBuilder filePathBuilder = new StringBuilder();
        Path currentRelativePath = Paths.get("");
        // Working directory
        filePathBuilder.append(currentRelativePath.toAbsolutePath().toString());
        SpMediaPathHandler spMediaPathHandler = spMedia2PathHandlerMap.get(request.getSpecialMediaType());
        spMediaPathHandler.appendPathForMedia(request, filePathBuilder);

        try {

            boolean sent = initUploadFileFlow(request, response, fileForUpload, filePathBuilder);

            if (!sent) {
                sendMediaUndeliveredMsgToUploader(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendMediaUndeliveredMsgToUploader(request);
        }
    }

    private boolean initUploadFileFlow(UploadFileRequest request, HttpServletResponse servletResponse, MultipartFile fileForUpload, StringBuilder filePathBuilder) throws Exception {
        //data.put(DataKeys.FILE_SIZE, String.valueOf(bytesLeft)); //TODO make sure we really don't need this
        String infoMsg = prepareFileUploadInfoMsg(request.getSpecialMediaType(), request.getMessageInitiaterId(), request.getDestinationId(), fileForUpload.getSize());
        logger.info(infoMsg);

        long bytesLeft = fileForUpload.getSize();
        // Preparing file placeholder
        File newFile = new File(filePathBuilder.toString());
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();

        BufferedOutputStream bos = null;
        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            DataInputStream dis = new DataInputStream(fileForUpload.getInputStream());

            logger.info("Reading data...");
            byte[] buf = new byte[1024 * 8];
            int bytesRead;
            while (bytesLeft > 0 && (bytesRead = dis.read(buf, 0, (int) Math.min(buf.length, bytesLeft))) != -1) {
                bos.write(buf, 0, bytesRead);
                bytesLeft -= bytesRead;
            }

            if (bytesLeft > 0)
                throw new IOException("Upload was stopped abruptly");
            else if (bytesLeft < 0)
                throw new IOException("Read too many bytes. Upload seems corrupted.");


            // Informing source (uploader) that the file is on the way
            Response response = new Response<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.UPLOAD_SUCCESS));
            sendResponse(servletResponse, response, HttpServletResponse.SC_OK);

            // Inserting the record of the file upload, retrieving back the commId
            Integer commId = insertFileUploadRecord(request);

            // Sending file to destination
            Map<DataKeys, Object> data = convertRequest2Map(request, filePathBuilder, commId);
            String destToken = dao.getUserRecord(request.getDestinationId()).getToken();
            String pushEventAction = PushEventKeys.PENDING_DOWNLOAD;
            return pushSender.sendPush(destToken, pushEventAction, data);
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    private Map<DataKeys, Object> convertRequest2Map(UploadFileRequest request, StringBuilder filePathBuilder, Integer commId) {
        Map<DataKeys, Object> data = new HashMap<>();
        data.put(DataKeys.COMM_ID, commId.toString());
        data.put(DataKeys.FILE_PATH_ON_SERVER, filePathBuilder.toString());
        data.put(DataKeys.SOURCE_ID, request.getSourceId());
        data.put(DataKeys.SOURCE_WITH_EXTENSION, request.getSourceId() + "." + request.getMediaFile().getExtension());
        data.put(DataKeys.FILE_SIZE, request.getMediaFile().getSize());
        data.put(DataKeys.SPECIAL_MEDIA_TYPE, request.getSpecialMediaType());
        data.put(DataKeys.DESTINATION_CONTACT_NAME, request.getDestinationContactName());
        data.put(DataKeys.FILE_TYPE, request.getMediaFile().getFileType());
        data.put(DataKeys.SOURCE_LOCALE, request.getSourceLocale());
        data.put(DataKeys.FILE_PATH_ON_SRC_SD, request.getFilePathOnSrcSd());
        data.put(DataKeys.EXTENSION, request.getMediaFile().getExtension());
        data.put(DataKeys.MD5, request.getMediaFile().getMd5());
        return data;
    }

    private Integer insertFileUploadRecord(UploadFileRequest request) throws SQLException {
        String md5 = request.getMediaFile().getMd5();
        String extension = request.getMediaFile().getExtension();
        MediaTransferDBO mediaTransferDBO = new MediaTransferDBO(
                request.getSpecialMediaType(),
                md5,
                request.getSourceId(),
                request.getDestinationId()
                ,new Date());

        Integer commId = dao.insertMediaTransferRecord(mediaTransferDBO, new MediaFileDBO(md5, extension, request.getMediaFile().getSize()));
        logger.info("commId returned:" + commId);
        return commId;
    }

    private String prepareFileUploadInfoMsg(SpecialMediaType specialMediaType, String messageInitiaterId, String destId, long fileSize) {
        return "Initiating file upload. [Source]:" + messageInitiaterId +
                ". [Destination]:" + destId + "." +
                " [Special Media Type]:" + specialMediaType +
                " [File size]:" +
                MediaFilesUtils.getFileSizeFormat(fileSize);
    }

    private void sendMediaUndeliveredMsgToUploader(UploadFileRequest request) {

        String messageInitiaterId = request.getMessageInitiaterId();
        String destId = request.getDestinationId();
        String destContact = request.getDestinationContactName();
        logger.severe("Upload from [Source]:" + messageInitiaterId + " to [Destination]:" + destId + " Failed.");

        LangStrings strings = stringsFactory.getStrings(request.getSourceLocale());
        String title = strings.media_undelivered_title();

        String dest = (!destContact.equals("") ? destContact : destId);
        String errMsg = String.format(strings.media_undelivered_body(), dest);

        String destHtml = "<b><font color=\"#00FFFF\">" + (!destContact.equals("") ? destContact : destId) + "</font></b>";
        String errMsgHtml = String.format(strings.media_undelivered_body(), destHtml);

        // Packing HTML string as push data
        HashMap<DataKeys, Object> replyData = new HashMap<>();
        replyData.put(DataKeys.HTML_STRING, errMsgHtml);

        String initiaterToken = usersDataAccess.getUserRecord(messageInitiaterId).getToken();

        // Informing source (uploader) that the file was not sent to destination
        pushSender.sendPush(initiaterToken, PushEventKeys.SHOW_ERROR, title, errMsg, replyData);
    }
}
