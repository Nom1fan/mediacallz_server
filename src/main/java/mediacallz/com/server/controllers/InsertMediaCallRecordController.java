package mediacallz.com.server.controllers;

import com.google.gson.Gson;
import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.dbos.MediaCallDBO;
import mediacallz.com.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public MessageToClient insertMediaCallRecord(HttpServletRequest request, HttpServletResponse response) {

        try {
            CallRecord callRecord = gson.fromJson(request.getParameter(DataKeys.CALL_RECORD.toString()), CallRecord.class);
            MediaCallDBO mediaCallDBO = prepareMediaCallDBO(callRecord);
            List<MediaFile> mediaFiles = prepareMediaFiles(callRecord);
            int callId = dao.insertMediaCallRecord(mediaCallDBO, mediaFiles);
            logger.info("Insert call record was successful. Call Id returned:[" + callId + "]");
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Insert call record failed. Exception:[" + e.getMessage() + "]", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.NO_ACTION_REQUIRED));

    }

    private List<MediaFile> prepareMediaFiles(final CallRecord callRecord) {
        LinkedList<MediaFile> mediaFiles = new LinkedList<>();
        if(callRecord.getVisualMediaFile()!=null)
            mediaFiles.add(callRecord.getVisualMediaFile());
        if(callRecord.getAudioMediaFile()!=null)
            mediaFiles.add(callRecord.getAudioMediaFile());
        return mediaFiles;
    }

    private MediaCallDBO prepareMediaCallDBO(CallRecord callRecord) {
        return new MediaCallDBO(
                callRecord.getSpecialMediaType(),
                callRecord.getVisualMd5(),
                callRecord.getAudioMd5(),
                callRecord.getSourceId(),
                callRecord.getDestinationId(),
                new Date());
    }
}
