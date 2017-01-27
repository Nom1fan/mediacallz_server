package com.mediacallz.server.logic;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.exceptions.DownloadRequestFailedException;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.push.PendingDownloadData;
import com.mediacallz.server.model.request.DownloadFileRequest;
import com.mediacallz.server.services.PushSender;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class DownloadLogic extends AbstractServerLogic {

    private LangStrings strings;
    private String messageInitiaterId;
    private String sourceId;
    private String destId;
    private String destContactName;
    private int commId;

    private final Dao dao;

    private final PushSender pushSender;

    private final UsersDataAccess usersDataAccess;

    private final MapperFacade mapperFacade;

    private HttpServletResponse response;

    private HttpServletRequest servletRequest;

    @Autowired
    public DownloadLogic(Dao dao, PushSender pushSender, UsersDataAccess usersDataAccess, MapperFacade mapperFacade) {
        this.dao = dao;
        this.pushSender = pushSender;
        this.usersDataAccess = usersDataAccess;
        this.mapperFacade = mapperFacade;
    }

    public void execute(DownloadFileRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        this.response = response;
        this.servletRequest = servletRequest;

        messageInitiaterId = request.getMessageInitiaterId();
        commId = request.getCommId();
        sourceId = request.getSourceId();
        destId = request.getDestinationId();
        destContactName = request.getDestinationContactName();
        String sourceLocale = request.getSourceLocale();
        String filePathOnServer = request.getFilePathOnServer();


        strings = stringsFactory.getStrings(sourceLocale);

        logger.info(messageInitiaterId + " is requesting download from:" + sourceId + ". File path on server:" + filePathOnServer + "...");

        initiateDownloadFlow(request);
    }

    private void initiateDownloadFlow(DownloadFileRequest request) {
        PendingDownloadData pendingDownloadData = mapperFacade.map(request, PendingDownloadData.class);
        try {
            initiateDownload(request.getFilePathOnServer());

        } catch (DownloadRequestFailedException | IOException e) {
            handleDownloadFailure(e, pendingDownloadData);
        }
    }

    private void initiateDownload(String filePathOnServer) throws DownloadRequestFailedException, IOException {
        BufferedInputStream bis = null;
        OutputStream os = response.getOutputStream();
        try {

            File fileForDownload = new File(filePathOnServer);
            // get MIME type of the file
            String mimeType = servletRequest.getServletContext().getMimeType(filePathOnServer);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            logger.info("MIME type: " + mimeType);

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) fileForDownload.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    fileForDownload.getName());
            response.setHeader(headerKey, headerValue);

            logger.info("Initiating data send...");

            DataOutputStream dos = new DataOutputStream(os);
            FileInputStream fis = new FileInputStream(fileForDownload);
            bis = new BufferedInputStream(fis);

            byte[] buf = new byte[1024 * 8];
            long fileSize = fileForDownload.length();
            long bytesToRead = fileSize;
            int bytesRead;
            while (bytesToRead > 0 && (bytesRead = bis.read(buf, 0, (int) Math.min(buf.length, bytesToRead))) != -1) {
                dos.write(buf, 0, bytesRead);
                bytesToRead -= bytesRead;
            }

            if (bytesToRead > 0)
                throw new IOException("download was stopped abruptly. " + bytesToRead + " out of " + fileSize + " bytes left.");

        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException ignored) {
                }
        }
    }

    private void handleDownloadFailure(Exception e, PendingDownloadData pendingDownloadData) {

        logger.severe("User " + messageInitiaterId + " download request failed. Exception:" + e.getMessage());

        String title = strings.media_undelivered_title();

        String dest = (!destContactName.equals("") ? destContactName : destId);
        String msgTransferFailed = String.format(strings.media_undelivered_body(), dest);

        // Informing sender that file did not reach destination
        logger.severe("Informing sender:" + sourceId + " that file did not reach destination:" + destId);
        String senderToken = usersDataAccess.getUserRecord(sourceId).getToken();
        boolean sent = pushSender.sendPush(senderToken, PushEventKeys.TRANSFER_FAILURE, title, msgTransferFailed, pendingDownloadData);

        if (!sent)
            logger.severe("Failed trying to Inform sender:" + sourceId + " that file did not reach destination:" + destId + ". Empty token");

        // Marking in communication history record that the transfer has failed
        char FALSE = '0';
        try {
            dao.updateMediaTransferRecord(commId, Dao.COL_TRANSFER_SUCCESS, FALSE);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

}
