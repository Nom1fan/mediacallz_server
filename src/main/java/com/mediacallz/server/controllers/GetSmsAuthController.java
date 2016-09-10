package com.mediacallz.server.controllers;

import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.lang.StringsFactory;
import com.mediacallz.server.model.*;
import com.mediacallz.server.services.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class GetSmsAuthController extends PreRegistrationController {

    private final String url = "/v1/GetSmsAuthCode";

    @Autowired
    private StringsFactory stringsFactory;

    @Autowired
    private SmsVerificationAccess smsVerificationAccess;

    @Autowired
    private SmsSender smsSender;

    @Value("${sms.maxCode}")
    private int maxCode;

    @Value("${sms.minCode}")
    private int minCode;

    private int code;

    @ResponseBody
    @RequestMapping(value = url, method = RequestMethod.POST)
    public MessageToClient getSmsAuthCode(HttpServletRequest request) throws IOException {

        code = generateSmsVerificationCode();

        String messageInitiaterId = request.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
        String internationalPhoneNumber = request.getParameter(DataKeys.INTERNATIONAL_PHONE_NUMBER.toString());
        String sourceLocale = request.getParameter(DataKeys.SOURCE_LOCALE.toString());

        LangStrings strings = stringsFactory.getStrings(sourceLocale);
        String msg = String.format(strings.your_verification_code(), code);

        boolean isOK = smsVerificationAccess.insertSmsVerificationCode(messageInitiaterId, code);

        if(isOK) {
            smsSender.sendSms(internationalPhoneNumber, msg);
            return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.GET_SMS_CODE_SUCCESS));
        }
        else {
            return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.GET_SMS_CODE_FAILURE));
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
