package mediacallz.com.server.controllers;

import mediacallz.com.server.database.SmsVerificationAccess;
import mediacallz.com.server.lang.LangStrings;
import mediacallz.com.server.lang.StringsFactory;
import mediacallz.com.server.model.request.GetSmsRequest;
import mediacallz.com.server.services.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
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

    @Value("${sms.maxcode}")
    private int maxCode;

    @Value("${sms.mincode}")
    private int minCode;

    @RequestMapping(value = url, method = RequestMethod.POST)
    public void getSmsAuthCode(@RequestBody GetSmsRequest request, HttpServletResponse response) throws IOException {

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
