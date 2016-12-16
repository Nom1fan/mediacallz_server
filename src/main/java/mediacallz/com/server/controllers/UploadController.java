package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.UsersDataAccess;
import mediacallz.com.server.database.dbos.MediaFileDBO;
import mediacallz.com.server.database.dbos.MediaTransferDBO;
import mediacallz.com.server.handlers.upload_controller.SpMediaPathHandler;
import mediacallz.com.server.lang.LangStrings;
import mediacallz.com.server.model.*;
import mediacallz.com.server.services.PushSender;
import mediacallz.com.server.utils.MediaFilesUtils;
import mediacallz.com.server.utils.ServletRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
 * Created by Mor on 12/09/2016.
 */
@Controller
public class UploadController extends AbstractController {

    @Autowired
    private UsersDataAccess usersDataAccess;

    @Autowired
    private PushSender pushSender;

    @Autowired
    private ServletRequestUtils servletRequestUtils;

    @Autowired
    private Dao dao;

    private final Map<SpecialMediaType, SpMediaPathHandler> spMedia2PathHandlerMap = new HashMap<>();

    @Autowired
    public void initMap(List<SpMediaPathHandler> spMediaPathHandlerList) {
        for (SpMediaPathHandler spMediaPathHandler : spMediaPathHandlerList) {
            spMedia2PathHandlerMap.put(spMediaPathHandler.getHandledSpMediaType(), spMediaPathHandler);
        }
    }


    @RequestMapping(value = "/v1/UploadFile", method = RequestMethod.POST)
    public void uploadFile(MultipartHttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) {

        MultipartFile fileForUpload = servletRequest.getFile("fileForUpload");

        Map<DataKeys, Object> data = servletRequestUtils.extractParametersMap(servletRequest);
        StringBuilder filePathBuilder = new StringBuilder();
        Path currentRelativePath = Paths.get("");
        // Working directory
        filePathBuilder.append(currentRelativePath.toAbsolutePath().toString());
        SpecialMediaType specialMediaType = SpecialMediaType.valueOf(data.get(DataKeys.SPECIAL_MEDIA_TYPE).toString());
        SpMediaPathHandler spMediaPathHandler = spMedia2PathHandlerMap.get(specialMediaType);
        spMediaPathHandler.appendPathForMedia(data, filePathBuilder);

        String messageInitiaterId = data.get(DataKeys.MESSAGE_INITIATER_ID).toString();
        String destId = data.get(DataKeys.DESTINATION_ID).toString();

        BufferedOutputStream bos = null;
        try {

            long bytesLeft = fileForUpload.getSize();
            data.put(DataKeys.FILE_SIZE, String.valueOf(bytesLeft));
            String infoMsg = prepareFileUploadInfoMsg(specialMediaType, messageInitiaterId, destId, bytesLeft);
            logger.info(infoMsg);

            // Preparing file placeholder
            File newFile = new File(filePathBuilder.toString());
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();

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
            MessageToClient response = new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.UPLOAD_SUCCESS));
            sendResponse(servletResponse, response, HttpServletResponse.SC_OK);

            // Inserting the record of the file upload, retrieving back the commId
            Integer commId = insertFileUploadRecord(data, specialMediaType, destId, fileForUpload.getSize());

            // Sending file to destination
            data.put(DataKeys.COMM_ID, commId.toString());
            data.put(DataKeys.FILE_PATH_ON_SERVER, filePathBuilder.toString());
            String destToken = dao.getUserRecord(destId).getToken();
            String pushEventAction = PushEventKeys.PENDING_DOWNLOAD;
            boolean sent = pushSender.sendPush(destToken, pushEventAction, data);

            if (!sent) {
                sendMediaUndeliveredMsgToUploader(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendMediaUndeliveredMsgToUploader(data);

        } finally {
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private Integer insertFileUploadRecord(Map<DataKeys, Object> data, SpecialMediaType specialMediaType, String destId, long fileSize) throws SQLException {
        String md5 = data.get(DataKeys.MD5).toString();
        String extension = data.get(DataKeys.EXTENSION).toString();
        MediaTransferDBO mediaTransferDBO = new MediaTransferDBO(
                specialMediaType,
                md5,
                data.get(DataKeys.SOURCE_ID).toString(),
                destId
                ,new Date());

        Integer commId = dao.insertMediaTransferRecord(mediaTransferDBO, new MediaFileDBO(md5, extension, fileSize));
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

    private void sendMediaUndeliveredMsgToUploader(Map<DataKeys, Object> data) {

        String messageInitiaterId = data.get(DataKeys.MESSAGE_INITIATER_ID).toString();
        String destId = data.get(DataKeys.DESTINATION_ID).toString();
        String destContact = data.get(DataKeys.DESTINATION_CONTACT_NAME).toString();
        logger.severe("Upload from [Source]:" + messageInitiaterId + " to [Destination]:" + destId + " Failed.");

        LangStrings strings = stringsFactory.getStrings(data.get(DataKeys.SOURCE_LOCALE).toString());
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
