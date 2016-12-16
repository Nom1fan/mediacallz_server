package mediacallz.com.server.controllers;

import mediacallz.com.server.database.UsersDataAccess;
import mediacallz.com.server.services.PushSender;
import mediacallz.com.server.utils.ServletRequestUtils;
import mediacallz.com.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public MessageToClient clearMedia(HttpServletRequest request, HttpServletResponse response) {

        Map<DataKeys,Object> data = servletRequestUtils.extractParametersMap(request);
        String destId = (String) data.get(DataKeys.DESTINATION_ID);
        String destToken = usersDataAccess.getUserRecord(destId).getToken();
        String pushEventAction = PushEventKeys.CLEAR_MEDIA;
        boolean sent = pushSender.sendPush(destToken, pushEventAction, data);

        if(sent) {
            return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.CLEAR_SENT));
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new MessageToClient <>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.CLEAR_FAILURE));
        }
    }
}
