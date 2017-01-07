package com.mediacallz.server.controllers;

import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.lang.StringsFactory;
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

    private final StringsFactory stringsFactory;

    private final SmsVerificationAccess smsVerificationAccess;

    private final SmsSender smsSender;

    @Value("${sms.maxcode}")
    private int maxCode;

    @Value("${sms.mincode}")
    private int minCode;

    @Autowired
    public GetSmsAuthController(StringsFactory stringsFactory, SmsVerificationAccess smsVerificationAccess, SmsSender smsSender) {
        this.stringsFactory = stringsFactory;
        this.smsVerificationAccess = smsVerificationAccess;
        this.smsSender = smsSender;
    }

    @RequestMapping(value = url, method = RequestMethod.POST)
    public void getSmsAuthCode(@Valid @RequestBody GetSmsRequest request, HttpServletResponse response) throws IOException {

        int code = generateSmsVerificationCode();

        String messageInitiaterId = request.getMessageInitiaterId();
        String internationalPhoneNumber = request.getInternationalPhoneNumber();
        String sourceLocale = request.getSourceLocale();

        LangStrings strings = stringsFactory.getStrings(sourceLocale);
        String msg = String.format(strings.your_verification_code(), code);

        boolean isOK = smsVerificationAccess.insertSmsVerificationCode(messageInitiaterId, code);

        if(isOK) {
            smsSender.sendSms(internationalPhoneNumber, msg);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int generateSmsVerificationCode() {
        Random random = new Random();
        return minCode + random.nextInt(maxCode - minCode);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
