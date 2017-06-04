package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.request.SendPushRequest;
import com.mediacallz.server.services.PushSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
@Slf4j
public class SendPushLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    private final PushSender pushSender;

    @Autowired
    public SendPushLogic(UsersDao usersDao, PushSender pushSender) {
        this.usersDao = usersDao;
        this.pushSender = pushSender;
    }

    public void execute(SendPushRequest request, HttpServletResponse response) throws IOException {
        String messageInitiaterId = request.getUser().getUid();
        String destId = request.getDestinationId();
        log.info(messageInitiaterId + " is sending " + destId + " a push notification:" + request);

        UserDBO userRecord = usersDao.getUserRecord(destId);
        if(userRecord == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User:" + destId + " was not found");
            return;
        }

        pushSender.sendPush(userRecord.getToken(), PushEventKeys.SHOW_MESSAGE, request.getTitle(), request.getMessage());
    }
}
