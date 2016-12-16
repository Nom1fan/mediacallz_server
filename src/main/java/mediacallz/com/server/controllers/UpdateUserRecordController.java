package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.dbos.UserDBO;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.MessageToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Mor on 04/10/2016.
 */
@Controller
public class UpdateUserRecordController  extends AbstractController {

    @Autowired
    private Dao dao;

    @ResponseBody
    @RequestMapping("/v1/UpdateUserRecord")
    public MessageToClient updateUserRecord(HttpServletRequest request, HttpServletResponse response) {

        String messageInitiaterId = request.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
        logger.info(messageInitiaterId + " is updating its record...");

        String androidVersion = request.getParameter(DataKeys.ANDROID_VERSION.toString());
        UserDBO userRecord = new UserDBO();
        userRecord.setAndroidVersion(androidVersion);

        HashMap<DataKeys,Object> replyData = new HashMap<>();
        try {
            dao.updateUserRecord(messageInitiaterId, userRecord);
            replyData.put(DataKeys.IS_UPDATE_SUCCESS, true);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Failed to update record of user: " + messageInitiaterId + ". [Exception]:" + (e.getMessage()!=null ? e.getMessage() : e));
            replyData.put(DataKeys.IS_UPDATE_SUCCESS, false);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return new MessageToClient<>(ClientActionType.UPDATE_RES, replyData);
    }
}
