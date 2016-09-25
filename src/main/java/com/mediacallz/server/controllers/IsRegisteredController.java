package com.mediacallz.server.controllers;

import com.mediacallz.server.database.UserDataAccess;
import com.mediacallz.server.model.ClientActionType;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.MessageToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 24/08/2016.
 */
@Controller
public class IsRegisteredController extends AbstractController {

    private final
    UserDataAccess userDataAccess;

    @Autowired
    public IsRegisteredController(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/IsRegistered", method = RequestMethod.POST)
    public MessageToClient isRegistered(HttpServletRequest request) {

        String messageInitiaterId = request.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
        String destId = request.getParameter(DataKeys.DESTINATION_ID.toString());
        logger.info(messageInitiaterId + " is checking if " + destId + " is logged in...");
        Map<DataKeys,Object> data = new HashMap<>();
        data.put(DataKeys.DESTINATION_ID, destId);
        data.put(DataKeys.IS_REGISTERED, userDataAccess.isRegistered(destId));

        return new MessageToClient<>(ClientActionType.IS_REGISTERED_RES, data);
    }
}
