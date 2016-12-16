package mediacallz.com.server.controllers;

import mediacallz.com.server.database.UsersDataAccess;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.MessageToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mor on 04/10/2016.
 */
@Controller
public class UnregisterController extends AbstractController {

    @Autowired
    private UsersDataAccess usersDataAccess;

    @RequestMapping("/v1/UnRegister")
    public void unregister(HttpServletRequest request, HttpServletResponse response) {

        String messageInitiaterId = request.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());

        try {
            HashMap<DataKeys, Object> replyData = new HashMap<>();
            replyData.put(DataKeys.IS_UNREGISTER_SUCCESS, true);
            sendResponse(response, new MessageToClient<>(ClientActionType.UNREGISTER_RES, replyData), HttpServletResponse.SC_OK); //TODO remove boolean, unregister always succeeds for user as long as connection exists. Backend operations are for the server only
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Failed to send user " + messageInitiaterId + " unregister reply");
        }
        String token = request.getParameter(DataKeys.PUSH_TOKEN.toString());
        usersDataAccess.unregisterUser(messageInitiaterId, token);
    }
}
