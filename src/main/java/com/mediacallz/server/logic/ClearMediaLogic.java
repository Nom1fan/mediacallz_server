package com.mediacallz.server.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.push.ClearMediaData;
import com.mediacallz.server.model.request.ClearMediaRequest;
import com.mediacallz.server.services.PushSender;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class ClearMediaLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    private final PushSender pushSender;

    private final MapperFacade mapperFacade;

    @Autowired
    public ClearMediaLogic(UsersDao usersDao, PushSender pushSender, MapperFacade mapperFacade) {
        this.usersDao = usersDao;
        this.pushSender = pushSender;
        this.mapperFacade = mapperFacade;
    }

    public void execute(ClearMediaRequest request, HttpServletResponse response) {
        String destId = request.getDestinationId();
        UserDBO userRecord = usersDao.getUserRecord(destId);
        boolean sentOK = false;
        if(userRecord!=null) {
            String destToken = userRecord.getToken();
            String pushEventAction = PushEventKeys.CLEAR_MEDIA;
            ClearMediaData clearMediaData = mapperFacade.map(request, ClearMediaData.class);
            sentOK = pushSender.sendPush(destToken, pushEventAction, clearMediaData);
        }

        if (!sentOK) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
