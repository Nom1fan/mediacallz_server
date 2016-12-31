package com.mediacallz.server.controllers;

import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.exceptions.DownloadRequestFailedException;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.model.request.DownloadFileRequest;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.utils.RequestUtils;
import com.mediacallz.server.database.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 20/09/2016.
 */
@Controller
public class DownloadController extends AbstractController {

    private LangStrings strings;
    private String messageInitiaterId;
    private String sourceId;
    private String destId;
    private String destContactName;
    private int commId;

    @Autowired
    private Dao dao;

    @Autowired
    private PushSender pushSender;

    @Autowired
    private UsersDataAccess usersDataAccess;

    @Autowired
    private RequestUtils requestUtils;

    private HttpServletResponse response;

    private HttpServletRequest servletRequest;

    @RequestMapping(value = "/v1/DownloadFile", method = RequestMethod.POST)
    public void downloadFile(@RequestBody DownloadFileRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        this.response = response;
        this.servletRequest = servletRequest;

        messageInitiaterId = request.getMessageInitiaterId();
        commId = request.getCommId();
        sourceId = request.getSourceId();
        destId = request.getDestinationId();
        destContactName = request.getDestinationContactName();
        String sourceLocale = request.getSourceLocale();
        String filePathOnServer = request.getFilePathOnServer();

        if (sourceLocale != null)
            strings = stringsFactory.getStrings(sourceLocale);

        logger.info(messageInitiaterId + " is requesting download from:" + sourceId + ". File path on server:" + filePathOnServer + "...");

        initiateDownloadFlow(request);
    }

    private void initiateDownloadFlow(DownloadFileRequest request) {
        try {
            initiateDownload(request.getFilePathOnServer());

            informSrcOfSuccess(request);

            // Marking in communication history record that the transfer was successful
            char TRUE = '1';
            dao.updateMediaTransferRecord(commId, Dao.COL_TRANSFER_SUCCESS, TRUE);

        } catch (DownloadRequestFailedException | SQLException | IOException e) {
            handleDownloadFailure(e);
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

            if(bytesToRead > 0)
                throw new IOException("download was stopped abruptly. " + bytesToRead + " out of " + fileSize + " bytes left.");

        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException ignored) {
                }
        }
    }

    // Informing source (uploader) that file received by user (downloader)
    private void informSrcOfSuccess(DownloadFileRequest request) {
        String title = strings.media_ready_title();
        String msg = String.format(strings.media_ready_body(), !destContactName.equals("") ? destContactName : destId);
        String token = usersDataAccess.getUserRecord(sourceId).getToken();
        boolean sent = pushSender.sendPush(token, PushEventKeys.TRANSFER_SUCCESS, title, msg, convertRequest2Map(request));
        if (!sent)
            logger.warning("Failed to inform user " + sourceId + " of transfer success to user: " + destId);
    }

    private void handleDownloadFailure(Exception e) {

        logger.severe("User " + messageInitiaterId + " download request failed. Exception:" + e.getMessage());

        String title = strings.media_undelivered_title();

        String dest = (!destContactName.equals("") ? destContactName : destId);
        String msgTransferFailed = String.format(strings.media_undelivered_body(), dest);

        String destHtml = "<b><font color=\"#00FFFF\">" + (!destContactName.equals("") ? destContactName : destId) + "</font></b>";
        String msgTransferFailedHtml = String.format(strings.media_undelivered_body(), destHtml);

        HashMap<DataKeys, Object> data = new HashMap<>();
        data.put(DataKeys.HTML_STRING, msgTransferFailedHtml);

        // Informing sender that file did not reach destination
        logger.severe("Informing sender:" + sourceId + " that file did not reach destination:" + destId);
        String senderToken = usersDataAccess.getUserRecord(sourceId).getToken();
        boolean sent = pushSender.sendPush(senderToken, PushEventKeys.SHOW_ERROR, title, msgTransferFailed, data);

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

    private Map<DataKeys,Object> convertRequest2Map(DownloadFileRequest request) {
        Map<DataKeys,Object> map = new HashMap<>();
        map.put(DataKeys.DESTINATION_ID, request.getDestinationId());
        map.put(DataKeys.SPECIAL_MEDIA_TYPE, request.getSpecialMediaType());
        map.put(DataKeys.FILE_TYPE, request.getFileType());
        map.put(DataKeys.FILE_PATH_ON_SRC_SD, request.getFilePathOnSrcSd());
        return map;
    }
}
