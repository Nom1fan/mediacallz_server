package mediacallz.com.server.controllers;

import com.google.gson.Gson;
import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.dbos.MediaCallDBO;
import mediacallz.com.server.model.*;
import mediacallz.com.server.model.request.InsertMediaCallRecordRequest;
import mediacallz.com.server.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Mor on 03/10/2016.
 */
@Controller
public class InsertMediaCallRecordController extends AbstractController {

    @Autowired
    private Dao dao;

    @Autowired
    private Gson gson;

    @ResponseBody
    @RequestMapping("/v1/InsertMediaCallRecord")
    public Response insertMediaCallRecord(@RequestBody InsertMediaCallRecordRequest request, HttpServletResponse response) {

        int callId = -1;
        try {
            boolean isValid = validateRequest(request, response);
            if (isValid) {
                CallRecord callRecord = request.getCallRecord();
                MediaCallDBO mediaCallDBO = prepareMediaCallDBO(callRecord);
                List<MediaFile> mediaFiles = prepareMediaFiles(callRecord);
                callId = dao.insertMediaCallRecord(mediaCallDBO, mediaFiles);
                logger.info("Insert call record was successful. Call Id returned:[" + callId + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Insert call record failed. Exception:[" + e.getMessage() + "]", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return new Response<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.NO_ACTION_REQUIRED, callId));
    }

    private boolean validateRequest(InsertMediaCallRecordRequest request, HttpServletResponse response) throws IOException {
        boolean isVisualMediaFileValid;
        boolean isAudioMediaFileValid;

        CallRecord callRecord = request.getCallRecord();
        MediaFile visualMediaFile = callRecord.getVisualMediaFile();
        MediaFile audioMediaFile = callRecord.getAudioMediaFile();

        if (sendErrIfNecessary(response, (visualMediaFile == null && audioMediaFile == null))) return false;
        isVisualMediaFileValid = visualMediaFile == null || isMediaFileValid(visualMediaFile);
        isAudioMediaFileValid = audioMediaFile == null || isMediaFileValid(audioMediaFile);
        if (sendErrIfNecessary(response, (!isVisualMediaFileValid || !isAudioMediaFileValid))) return false;
        boolean areOtherParamsValid = callRecord.getDestinationId() != null && callRecord.getSourceId() != null && callRecord.getSpecialMediaType() != null;
        return !sendErrIfNecessary(response, !areOtherParamsValid);
    }

    private boolean sendErrIfNecessary(HttpServletResponse response, boolean isInvalid) throws IOException {
        if (isInvalid) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return true;
        }
        return false;
    }

    private boolean isMediaFileValid(MediaFile mediaFile) {
        return !isNull(mediaFile.getMd5()) && !isNull(mediaFile.getExtension()) && !isNull(mediaFile.getFileType()) && mediaFile.getSize() >= 0;

    }

    private List<MediaFile> prepareMediaFiles(final CallRecord callRecord) {
        LinkedList<MediaFile> mediaFiles = new LinkedList<>();
        if (callRecord.getVisualMediaFile() != null)
            mediaFiles.add(callRecord.getVisualMediaFile());
        if (callRecord.getAudioMediaFile() != null)
            mediaFiles.add(callRecord.getAudioMediaFile());
        return mediaFiles;
    }

    private MediaCallDBO prepareMediaCallDBO(CallRecord callRecord) {
        MediaFile visualMediaFile = callRecord.getVisualMediaFile();
        MediaFile audioMediaFile = callRecord.getAudioMediaFile();
        String visualMediaFileMd5 = visualMediaFile == null ? null : visualMediaFile.getMd5();
        String audioMediaFileMd5 = audioMediaFile == null ? null : audioMediaFile.getMd5();

        return new MediaCallDBO(
                callRecord.getSpecialMediaType(),
                visualMediaFileMd5,
                audioMediaFileMd5,
                callRecord.getSourceId(),
                callRecord.getDestinationId(),
                new Date());
    }

    private boolean isNull(Object o) {
        return o == null;
    }
}
