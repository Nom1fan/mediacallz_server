package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.request.UpdateUserRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;

/**
 * Created by Mor on 04/10/2016.
 */
@Controller
public class UpdateUserRecordController extends AbstractController {

    private final Dao dao;

    @Autowired
    public UpdateUserRecordController(Dao dao) {
        this.dao = dao;
    }

    @ResponseBody
    @RequestMapping("/v1/UpdateUserRecord")
    public void updateUserRecord(@Valid @RequestBody UpdateUserRecordRequest request, HttpServletResponse response) {

        String messageInitiaterId = request.getMessageInitiaterId();
        logger.info(messageInitiaterId + " is updating its record...");

        String androidVersion = request.getAndroidVersion();
        String iosVersion = request.getIosVersion();
        String appVersion = request.getAppVersion();
        UserDBO userRecord = new UserDBO();
        userRecord.setAndroidVersion(androidVersion);
        userRecord.setIOSVersion(iosVersion);
        userRecord.setAppVersion(appVersion);

        try {
            dao.updateUserRecord(messageInitiaterId, userRecord);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Failed to update record of user: " + messageInitiaterId + ". [Exception]:" + (e.getMessage()!=null ? e.getMessage() : e));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
