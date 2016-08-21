package com.mediacallz.server.controllers;

import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.lang.StringsFactory;
import com.mediacallz.server.model.*;
import com.mediacallz.server.services.SmsSender;
import com.mediacallz.server.utils.RandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class UsersController extends AbstractController {

    @Autowired
    private StringsFactory stringsFactory;

    @Autowired
    private SmsVerificationAccess smsVerificationAccess;

    @Autowired
    private SmsSender smsSender;

    private static final int MIN_CODE = 1000;
    private static final int MAX_CODE = 9999;

    @ResponseBody
    @RequestMapping(value = "/v1/GetSmsAuthCode", method = RequestMethod.POST)
    public MessageToClient getSmsAuthCode(HttpServletRequest request) throws IOException {

        logger.info("Request params:" + request.getParameterMap());

        String messageInitiaterId = request.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
        String internationalPhoneNumber = request.getParameter(DataKeys.INTERNATIONAL_PHONE_NUMBER.toString());
        String sourceLocale = request.getParameter(DataKeys.SOURCE_LOCALE.toString());

        int code = RandUtils.getRand(MIN_CODE, MAX_CODE);

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
}
