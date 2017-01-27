package com.mediacallz.server.controllers;

import com.mediacallz.server.logic.NotifyMediaClearedLogic;
import com.mediacallz.server.logic.NotifyMediaReadyLogic;
import com.mediacallz.server.model.request.NotifyMediaClearedRequest;
import com.mediacallz.server.model.request.NotifyMediaReadyRequest;
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
public class NotifyMediaReadyController extends AbstractController {

    private final NotifyMediaReadyLogic logic;

    @Autowired
    public NotifyMediaReadyController(NotifyMediaReadyLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping("/v1/NotifyMediaReady")
    public void notifyMediaCleared(@Valid @RequestBody NotifyMediaReadyRequest request, HttpServletResponse response) {
        logic.execute(request, response);
    }
}
