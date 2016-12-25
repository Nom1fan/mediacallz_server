package com.mediacallz.server.controllers;

import com.mediacallz.server.model.ClientActionType;
import com.mediacallz.server.model.EventReport;
import com.mediacallz.server.model.EventType;
import com.mediacallz.server.model.MediaFile;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.MediaCallDBO;
import com.mediacallz.server.model.dto.MediaCallDTO;
import com.mediacallz.server.model.request.InsertMediaCallRecordRequest;
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

    @ResponseBody
    @RequestMapping("/v1/InsertMediaCallRecord")
    public Response insertMediaCallRecord(@RequestBody InsertMediaCallRecordRequest request, HttpServletResponse response) {

        int callId = -1;
        try {
            boolean isValid = validateRequest(request, response);
            if (isValid) {
                MediaCallDTO mediaCallDTO = request.getMediaCallDTO();
                MediaCallDBO mediaCallDBO = prepareMediaCallDBO(mediaCallDTO);
                List<MediaFile> mediaFiles = prepareMediaFiles(mediaCallDTO);
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

        MediaCallDTO mediaCallDTO = request.getMediaCallDTO();
        MediaFile visualMediaFile = mediaCallDTO.getVisualMediaFile();
        MediaFile audioMediaFile = mediaCallDTO.getAudioMediaFile();

        if (sendErrIfNecessary(response, (visualMediaFile == null && audioMediaFile == null))) return false;
        isVisualMediaFileValid = visualMediaFile == null || isMediaFileValid(visualMediaFile);
        isAudioMediaFileValid = audioMediaFile == null || isMediaFileValid(audioMediaFile);
        if (sendErrIfNecessary(response, (!isVisualMediaFileValid || !isAudioMediaFileValid))) return false;
        boolean areOtherParamsValid = mediaCallDTO.getDestinationId() != null && mediaCallDTO.getSourceId() != null && mediaCallDTO.getSpecialMediaType() != null;
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

    private List<MediaFile> prepareMediaFiles(final MediaCallDTO mediaCallDTO) {
        LinkedList<MediaFile> mediaFiles = new LinkedList<>();
        if (mediaCallDTO.getVisualMediaFile() != null)
            mediaFiles.add(mediaCallDTO.getVisualMediaFile());
        if (mediaCallDTO.getAudioMediaFile() != null)
            mediaFiles.add(mediaCallDTO.getAudioMediaFile());
        return mediaFiles;
    }

    private MediaCallDBO prepareMediaCallDBO(MediaCallDTO mediaCallDTO) {
        MediaFile visualMediaFile = mediaCallDTO.getVisualMediaFile();
        MediaFile audioMediaFile = mediaCallDTO.getAudioMediaFile();
        String visualMediaFileMd5 = visualMediaFile == null ? null : visualMediaFile.getMd5();
        String audioMediaFileMd5 = audioMediaFile == null ? null : audioMediaFile.getMd5();

        return new MediaCallDBO(
                mediaCallDTO.getSpecialMediaType(),
                visualMediaFileMd5,
                audioMediaFileMd5,
                mediaCallDTO.getSourceId(),
                mediaCallDTO.getDestinationId(),
                new Date());
    }

    private boolean isNull(Object o) {
        return o == null;
    }
}
