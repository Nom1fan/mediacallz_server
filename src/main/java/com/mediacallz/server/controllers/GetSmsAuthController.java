package com.mediacallz.server.controllers;

import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.lang.StringsFactory;
import com.mediacallz.server.logic.GetSmsAuthLogic;
import com.mediacallz.server.model.request.GetSmsRequest;
import com.mediacallz.server.services.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class GetSmsAuthController extends PreRegistrationController {

    private final String url = "/v1/GetSmsAuthCode";

    private final GetSmsAuthLogic getSmsAuthLogic;

    @Autowired
    public GetSmsAuthController(GetSmsAuthLogic getSmsAuthLogic) {
        this.getSmsAuthLogic = getSmsAuthLogic;
    }

    @RequestMapping(value = url, method = RequestMethod.POST)
    public void getSmsAuthCode(@Valid @RequestBody GetSmsRequest request, HttpServletResponse response) throws IOException {
        getSmsAuthLogic.execute(request, response);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
