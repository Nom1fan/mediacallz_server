package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.model.push.AttachMediaData;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.push.PushResponse;
import com.mediacallz.server.model.request.AttachMediaRequest;
import com.mediacallz.server.services.PushSender;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

/**
 * Created by Mor on 1/16/2017.
 */
@Slf4j
@Component
public class AttachMediaLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    private final PushSender pushSender;

    private MapperFacade mapperFacade;

    @Autowired
    public AttachMediaLogic(UsersDao usersDao, PushSender pushSender, MapperFacade mapperFacade) {
        this.usersDao = usersDao;
        this.pushSender = pushSender;
        this.mapperFacade = mapperFacade;
    }

    public void execute(AttachMediaRequest request, HttpServletResponse response) throws IOException {

        boolean isValid;
        String destinationId = request.getDestinationId();
        isValid = isDestinationIdValid(request.getUser().getUid(), destinationId);
        if (!isValid) {
            response.sendError(SC_BAD_REQUEST, "Invalid destination ID. Cannot attach media to self.");
            return;
        }

        UserDBO userRecord = usersDao.getUserRecord(destinationId);
        isValid = isUserRecordValid(userRecord);
        if (!isValid) {
            response.sendError(SC_BAD_REQUEST, String.format("Cannot find user %s", destinationId));
            return;
        }

        AttachMediaData attachMediaData = mapperFacade.map(request, AttachMediaData.class);
        pushSender.sendPush(userRecord.getToken(), PushEventKeys.ATTACH_MEDIA, attachMediaData);
        handlePushResponse(pushSender.getPushResponse(), response, destinationId);
    }

    private void handlePushResponse(PushResponse pushResponse, HttpServletResponse response, String destinationId) throws IOException {
        if (pushResponse.getStatusCode() != SC_OK) {
            if (pushResponse.getStatusCode() == SC_NOT_FOUND) {
                response.sendError(SC_BAD_REQUEST, String.format("Cannot find user %s's push token", destinationId));
            } else {
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private boolean isUserRecordValid(UserDBO userRecord) {
        return !(userRecord == null);
    }

    private boolean isDestinationIdValid(String sourceId, String destinationId) {
        return !sourceId.equals(destinationId);
    }
}
