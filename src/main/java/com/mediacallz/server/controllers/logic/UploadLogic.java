package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.controllers.handlers.upload.controller.MediaPathHandler;
import com.mediacallz.server.controllers.handlers.upload.controller.MediaPathHandlersFactory;
import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.MediaFileDBO;
import com.mediacallz.server.db.dbo.MediaTransferDBO;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.push.PendingDownloadData;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.request.UploadFileRequest;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.utils.MediaFileUtils;
import com.mediacallz.server.utils.SpecialMediaTypeUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Mor on 1/16/2017.
 */
@Component
@Slf4j
public class UploadLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    private final PushSender pushSender;

    private final Dao dao;

    private final MapperFacade mapperFacade;


    @Autowired
    private MediaFileUtils mediaFileUtils;

    @Autowired
    private MediaPathHandlersFactory mediaPathHandlersFactory;

    @Autowired
    private SpecialMediaTypeUtils specialMediaTypeUtils;

    @Autowired
    public UploadLogic(UsersDao usersDao, PushSender pushSender, Dao dao, MapperFacade mapperFacade) {
        this.usersDao = usersDao;
        this.pushSender = pushSender;
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public void execute(MultipartFile fileForUpload, UploadFileRequest request, HttpServletResponse response) {

        MediaPathHandler mediaPathHandler = mediaPathHandlersFactory.getPathHandler(request.getSpecialMediaType());
        String uploadFilePath = mediaPathHandler.appendPathForMedia(request);

        try {
            initUploadFileFlow(request, response, fileForUpload, uploadFilePath);

            // Inserting the record of the file upload, retrieving back the commId
            Integer commId = insertFileUploadRecord(request);

            if (!specialMediaTypeUtils.isDefaultMediaType(request.getSpecialMediaType())) {
                // Sending file to destination
                PendingDownloadData pushData = mapperFacade.map(request, PendingDownloadData.class);
                pushData.setCommId(commId);
                pushData.setFilePathOnServer(uploadFilePath);
                String destToken = dao.getUserRecord(request.getDestinationId()).getToken();
                pushSender.sendPush(destToken, PushEventKeys.PENDING_DOWNLOAD, pushData);
            }

            String folder = new File(uploadFilePath).getParent();
            mediaFileUtils.deleteFilesIfNecessary(folder, request.getMediaFile());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendMediaUndeliveredMsgToUploader(request);
        }
    }

    private void initUploadFileFlow(UploadFileRequest request, HttpServletResponse servletResponse, MultipartFile fileForUpload, String uploadedFilePath) throws Exception {
        logFileUploadInfoMsg(request);
        boolean success = false;

        long bytesLeft = fileForUpload.getSize();
        // Preparing file placeholder
        File newFile = new File(uploadedFilePath);
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();

        BufferedOutputStream bos = null;
        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            DataInputStream dis = new DataInputStream(fileForUpload.getInputStream());

            log.info("Reading data...");
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

        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    private Integer insertFileUploadRecord(UploadFileRequest request) throws SQLException {
        MediaTransferDBO mediaTransferDBO = mapperFacade.map(request, MediaTransferDBO.class);
        mediaTransferDBO.setDatetime(new Date());
        MediaFileDBO mediaFileDBO = request.getMediaFile().toInternal(mapperFacade);
        Integer commId = dao.insertMediaTransferRecord(mediaTransferDBO, mediaFileDBO);
        log.info("commId returned:" + commId);
        return commId;
    }

    private void logFileUploadInfoMsg(UploadFileRequest request) {
        String infoMsg = "Initiating file upload. [Source]:" + request.getUser().getUid() +
                ". [Destination]:" + request.getDestinationId() + "." +
                " [Special Media Type]:" + request.getSpecialMediaType() +
                " [File size]:" +
                mediaFileUtils.getFileSizeFormat(request.getMediaFile().getSize());
        log.info(infoMsg);
    }

    private void sendMediaUndeliveredMsgToUploader(UploadFileRequest request) {

        String messageInitiaterId = request.getUser().getUid();
        String destId = request.getDestinationId();
        String destContact = request.getDestinationContactName();
        log.error("Upload from [Source]:" + messageInitiaterId + " to [Destination]:" + destId + " Failed.");

        LangStrings strings = stringsFactory.getStrings(request.getLocale());
        String title = strings.media_undelivered_title();

        String dest = (!destContact.equals("") ? destContact : destId);
        String errMsg = String.format(strings.media_undelivered_body(), dest);

        String uploaderToken = usersDao.getUserRecord(messageInitiaterId).getToken();

        // Informing source (uploader) that the file was not sent to destination
        PendingDownloadData pendingDownloadData = mapperFacade.map(request, PendingDownloadData.class);
        pushSender.sendPush(uploaderToken, PushEventKeys.TRANSFER_FAILURE, title, errMsg, pendingDownloadData);
    }
}
