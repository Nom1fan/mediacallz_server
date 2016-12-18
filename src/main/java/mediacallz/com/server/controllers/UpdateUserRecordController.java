package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.dbos.UserDBO;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.request.UpdateUserRecordRequest;
import mediacallz.com.server.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Response updateUserRecord(@RequestBody UpdateUserRecordRequest request, HttpServletResponse response) {

        String messageInitiaterId = request.getMessageInitiaterId();
        logger.info(messageInitiaterId + " is updating its record...");

        String androidVersion = request.getAndroidVersion();
        String iosVersion = request.getIosVersion();
        String appVersion = request.getAppVersion();
        UserDBO userRecord = new UserDBO();
        userRecord.setAndroidVersion(androidVersion);
        userRecord.setIOSVersion(iosVersion);
        userRecord.setAppVersion(appVersion);

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

        return new Response<>(ClientActionType.UPDATE_RES, replyData);
    }
}
