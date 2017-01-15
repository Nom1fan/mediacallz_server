package com.mediacallz.server.logic;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.request.UpdateUserRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class UpdateUserRecordLogic extends AbstractServerLogic {

    private final Dao dao;

    @Autowired
    public UpdateUserRecordLogic(Dao dao) {
        this.dao = dao;
    }

    public void execute(UpdateUserRecordRequest request, HttpServletResponse response) {
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
