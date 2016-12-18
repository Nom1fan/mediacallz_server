package mediacallz.com.server.controllers;

import mediacallz.com.server.database.UsersDataAccess;
import mediacallz.com.server.database.dbos.UserDBO;
import mediacallz.com.server.model.*;
import mediacallz.com.server.model.request.ClearMediaRequest;
import mediacallz.com.server.model.response.Response;
import mediacallz.com.server.services.PushSender;
import mediacallz.com.server.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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
    private RequestUtils requestUtils;

    @ResponseBody
    @RequestMapping("/v1/ClearMedia")
    public Response clearMedia(@RequestBody ClearMediaRequest request, HttpServletResponse response) {

        String destId = request.getDestinationId();
        UserDBO userRecord = usersDataAccess.getUserRecord(destId);
        boolean sentOK = false;
        if(userRecord!=null) {
            String destToken = userRecord.getToken();
            String pushEventAction = PushEventKeys.CLEAR_MEDIA;
            sentOK = pushSender.sendPush(destToken, pushEventAction, convertRequest2Map(request));
        }

        if (sentOK) {
            return new Response<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.CLEAR_SENT));
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Response<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.CLEAR_FAILURE));
        }
    }

    private static Map<DataKeys,Object> convertRequest2Map(ClearMediaRequest request) {
        HashMap<DataKeys,Object> resultMap = new HashMap<>();
            resultMap.put(DataKeys.SPECIAL_MEDIA_TYPE, request.getSpecialMediaType());
            resultMap.put(DataKeys.SOURCE_ID, request.getSourceId());
            resultMap.put(DataKeys.DESTINATION_ID, request.getDestinationId());
        return resultMap;
    }
}
