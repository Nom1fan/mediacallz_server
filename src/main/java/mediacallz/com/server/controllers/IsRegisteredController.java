package mediacallz.com.server.controllers;

import mediacallz.com.server.database.UsersDataAccess;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.request.IsRegisteredRequest;
import mediacallz.com.server.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 24/08/2016.
 */
@Controller
public class IsRegisteredController extends AbstractController {

    private final
    UsersDataAccess usersDataAccess;

    @Autowired
    public IsRegisteredController(UsersDataAccess usersDataAccess) {
        this.usersDataAccess = usersDataAccess;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/IsRegistered", method = RequestMethod.POST)
    public Response isRegistered(@RequestBody IsRegisteredRequest request) {

        String messageInitiaterId = request.getMessageInitiaterId();
        String destId = request.getDestinationId();
        logger.info(messageInitiaterId + " is checking if " + destId + " is logged in...");
        Map<DataKeys,Object> data = new HashMap<>();
        data.put(DataKeys.DESTINATION_ID, destId);
        data.put(DataKeys.IS_REGISTERED, usersDataAccess.isRegistered(destId));

        return new Response<>(ClientActionType.IS_REGISTERED_RES, data);
    }
}
