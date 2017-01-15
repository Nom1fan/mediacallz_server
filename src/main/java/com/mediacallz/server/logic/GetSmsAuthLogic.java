package com.mediacallz.server.logic;

import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.lang.StringsFactory;
import com.mediacallz.server.model.request.GetSmsRequest;
import com.mediacallz.server.services.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class GetSmsAuthLogic extends AbstractServerLogic {

    private final SmsVerificationAccess smsVerificationAccess;

    private final SmsSender smsSender;

    @Value("${sms.maxcode}")
    private int maxCode;

    @Value("${sms.mincode}")
    private int minCode;

    @Autowired
    public GetSmsAuthLogic(SmsVerificationAccess smsVerificationAccess, SmsSender smsSender) {
        this.smsVerificationAccess = smsVerificationAccess;
        this.smsSender = smsSender;
    }

    public void execute(GetSmsRequest request, HttpServletResponse response) {
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
}
