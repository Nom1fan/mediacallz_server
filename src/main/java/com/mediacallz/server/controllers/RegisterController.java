package com.mediacallz.server.controllers;

import com.mediacallz.server.logic.RegisterLogic;
import com.mediacallz.server.model.request.RegisterRequest;
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
public class RegisterController extends PreRegistrationController {

    private final String url = "/v1/Register";

    private final RegisterLogic logic;

    @Autowired
    public RegisterController(RegisterLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping(value = url, method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) throws IOException {
        logic.execute(request, response);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
