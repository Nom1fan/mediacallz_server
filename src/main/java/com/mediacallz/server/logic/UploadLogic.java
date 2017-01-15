package com.mediacallz.server.logic;

import com.google.gson.Gson;
import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.database.dbo.MediaFileDBO;
import com.mediacallz.server.database.dbo.MediaTransferDBO;
import com.mediacallz.server.handlers.upload_controller.SpMediaPathHandler;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.model.push.PendingDownloadData;
import com.mediacallz.server.model.request.UploadFileRequest;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.utils.MediaFilesUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 1/16/2017.
 */
@Component
public class UploadLogic extends AbstractServerLogic {

    private final UsersDataAccess usersDataAccess;

    private final PushSender pushSender;

    private final Dao dao;

    private final MapperFacade mapperFacade;

    private final Map<SpecialMediaType, SpMediaPathHandler> spMedia2PathHandlerMap = new HashMap<>();

    @Autowired
    public void initMap(List<SpMediaPathHandler> spMediaPathHandlerList) {
        for (SpMediaPathHandler spMediaPathHandler : spMediaPathHandlerList) {
            spMedia2PathHandlerMap.put(spMediaPathHandler.getHandledSpMediaType(), spMediaPathHandler);
        }
    }

    @Autowired
    public UploadLogic(UsersDataAccess usersDataAccess, PushSender pushSender, Dao dao, Gson gson, MapperFacade mapperFacade) {
        this.usersDataAccess = usersDataAccess;
        this.pushSender = pushSender;
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public void execute(MultipartFile fileForUpload, UploadFileRequest request, HttpServletResponse response) {
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
        logFileUploadInfoMsg(request);

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
            servletResponse.setStatus(HttpServletResponse.SC_OK);

            // Inserting the record of the file upload, retrieving back the commId
            Integer commId = insertFileUploadRecord(request);

            // Sending file to destination
            PendingDownloadData pushData = mapperFacade.map(request, PendingDownloadData.class);
            pushData.setCommId(commId);
            pushData.setFilePathOnServer(filePathBuilder.toString());
            String destToken = dao.getUserRecord(request.getDestinationId()).getToken();
            return pushSender.sendPush(destToken, PushEventKeys.PENDING_DOWNLOAD, pushData);
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    private Integer insertFileUploadRecord(UploadFileRequest request) throws SQLException {
        MediaTransferDBO mediaTransferDBO = mapperFacade.map(request, MediaTransferDBO.class);
        mediaTransferDBO.setDatetime(new Date());
        MediaFileDBO mediaFileDBO = request.getMediaFileDTO().toInternal(mapperFacade);
        Integer commId = dao.insertMediaTransferRecord(mediaTransferDBO, mediaFileDBO);
        logger.info("commId returned:" + commId);
        return commId;
    }

    private void logFileUploadInfoMsg(UploadFileRequest request) {
        String infoMsg = "Initiating file upload. [Source]:" + request.getMessageInitiaterId() +
                ". [Destination]:" + request.getDestinationId() + "." +
                " [Special Media Type]:" + request.getSpecialMediaType() +
                " [File size]:" +
                MediaFilesUtils.getFileSizeFormat(request.getMediaFileDTO().getSize());
        logger.info(infoMsg);
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

        String uploaderToken = usersDataAccess.getUserRecord(messageInitiaterId).getToken();

        // Informing source (uploader) that the file was not sent to destination
        PendingDownloadData pendingDownloadData = mapperFacade.map(request, PendingDownloadData.class);
        pushSender.sendPush(uploaderToken, PushEventKeys.TRANSFER_FAILURE, title, errMsg, pendingDownloadData);
    }
}
