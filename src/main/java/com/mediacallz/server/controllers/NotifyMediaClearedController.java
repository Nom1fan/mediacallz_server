package com.mediacallz.server.controllers;

import com.mediacallz.server.logic.NotifyMediaClearedLogic;
import com.mediacallz.server.model.request.NotifyMediaClearedRequest;
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
public class NotifyMediaClearedController extends AbstractController {

    private final NotifyMediaClearedLogic logic;

    @Autowired
    public NotifyMediaClearedController(NotifyMediaClearedLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping("/v1/NotifyMediaCleared")
    public void notifyMediaCleared(@Valid @RequestBody NotifyMediaClearedRequest request, HttpServletResponse response) {
        logic.execute(request, response);
    }
}
