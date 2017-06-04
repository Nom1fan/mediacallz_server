package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.SendPushLogic;
import com.mediacallz.server.model.request.SendPushRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class SendPushController extends AbstractController {

    private final String url = "/v1/SendPush";

    private final SendPushLogic logic;

    @Autowired
    public SendPushController(SendPushLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping(value = url, method = RequestMethod.POST)
    public void register(@Valid @RequestBody SendPushRequest request, HttpServletResponse response) throws IOException {
        logic.execute(request, response);
    }
}
