package com.mediacallz.server.controllers;

import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.model.*;
import com.mediacallz.server.model.push.ClearMediaData;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.request.ClearMediaRequest;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Mor on 03/10/2016.
 */
@Controller
public class ClearMediaController extends AbstractController {

    private final UsersDataAccess usersDataAccess;

    private final PushSender pushSender;

    private final MapperFacade mapperFacade;

    @Autowired
    public ClearMediaController(UsersDataAccess usersDataAccess, PushSender pushSender, MapperFacade mapperFacade) {
        this.usersDataAccess = usersDataAccess;
        this.pushSender = pushSender;
        this.mapperFacade = mapperFacade;
    }

    @ResponseBody
    @RequestMapping("/v1/ClearMedia")
    public void clearMedia(@Valid @RequestBody ClearMediaRequest request, HttpServletResponse response) {

        String destId = request.getDestinationId();
        UserDBO userRecord = usersDataAccess.getUserRecord(destId);
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
