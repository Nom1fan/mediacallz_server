package com.mediacallz.server.controllers;

import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.model.request.UnRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Mor on 04/10/2016.
 */
@Controller
public class UnregisterController extends AbstractController {

    private final UsersDataAccess usersDataAccess;

    @Autowired
    public UnregisterController(UsersDataAccess usersDataAccess) {
        this.usersDataAccess = usersDataAccess;
    }

    @RequestMapping("/v1/UnRegister")
    public void unregister(@Valid @RequestBody UnRegisterRequest request) {
        String messageInitiaterId = request.getMessageInitiaterId();
        String token = request.getPushToken();
        usersDataAccess.unregisterUser(messageInitiaterId, token);
    }
}
