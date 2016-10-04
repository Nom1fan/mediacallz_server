package com.mediacallz.server.controllers;

import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.model.*;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.utils.ServletRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 03/10/2016.
 */
@Controller
public class ClearMediaController extends AbstractController {

    @Autowired
    private UsersDataAccess usersDataAccess;

    @Autowired
    private PushSender pushSender;

    @Autowired
    private ServletRequestUtils servletRequestUtils;

    @ResponseBody
    @RequestMapping("/v1/ClearMedia")
    public MessageToClient clearMedia(HttpServletRequest request) {

        Map<DataKeys,Object> data = servletRequestUtils.extractParametersMap(request);
        String destId = (String) data.get(DataKeys.DESTINATION_ID);
        String destToken = usersDataAccess.getUserRecord(destId).getToken();
        String pushEventAction = PushEventKeys.CLEAR_MEDIA;
        boolean sent = pushSender.sendPush(destToken, pushEventAction, data);

        HashMap<DataKeys,Object> replyData = new HashMap<>();
        if(sent) {
            replyData.put(DataKeys.EVENT_REPORT, new EventReport(EventType.CLEAR_SENT));
            return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, replyData);
        }
        else {
            replyData.put(DataKeys.EVENT_REPORT, new EventReport(EventType.CLEAR_FAILURE));
            return new MessageToClient <>(ClientActionType.TRIGGER_EVENT, replyData);
        }
    }
}
