package com.mediacallz.server.controllers;

import com.google.gson.Gson;
import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbos.MediaCallDBO;
import com.mediacallz.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
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
    public MessageToClient insertMediaCallRecord(HttpServletRequest request) {

        try {
            CallRecord callRecord = gson.fromJson(request.getParameter(DataKeys.CALL_RECORD.toString()), CallRecord.class);
            MediaCallDBO mediaCallDBO = prepareMediaCallDBO(callRecord);
            List<MediaFile> mediaFiles = prepareMediaFiles(callRecord);
            int callId = dao.insertMediaCallRecord(mediaCallDBO, mediaFiles);
            logger.info("Insert call record was successful. Call Id returned:[" + callId + "]");
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Insert call record failed. Exception:[" + e.getMessage() + "]", e);
        }
        return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.NO_ACTION_REQUIRED));

    }

    private List<MediaFile> prepareMediaFiles(@RequestBody final CallRecord callRecord) {
        return new LinkedList<MediaFile>() {{
            add(callRecord.getVisualMediaFile());
            add(callRecord.getAudioMediaFile()) ;
        }};
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
