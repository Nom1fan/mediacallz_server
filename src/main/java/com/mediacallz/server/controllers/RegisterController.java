package com.mediacallz.server.controllers;

import com.mediacallz.server.model.ClientActionType;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.model.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class RegisterController extends PreRegistrationController {

    private final String url = "/v1/Register";

    @Autowired
    SmsVerificationAccess smsVerificationAccess;

    @Autowired
    Dao dao;

    @ResponseBody
    @RequestMapping(value = url, method = RequestMethod.POST)
    public Response register(@RequestBody RegisterRequest request, HttpServletResponse response) throws IOException {
        String messageInitiaterId = request.getMessageInitiaterId();
        logger.info("[User]:" + messageInitiaterId + " is attempting to register.");

        int smsCode = request.getSmsCode();
        int expectedSmsCode = smsVerificationAccess.getSmsVerificationCode(messageInitiaterId);

        if (smsCode != SmsVerificationAccess.NO_SMS_CODE && smsCode == expectedSmsCode) {
            try {
                registerUser(request);
            } catch (SQLException e) {
                handleException(messageInitiaterId, response, e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch(DuplicateKeyException e) {
                handleException(messageInitiaterId, response, e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            registrationRejected(messageInitiaterId, response, smsCode, expectedSmsCode);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return new Response<>(ClientActionType.REGISTER_RES);
    }

    //region Assisting methods
    private void registrationRejected(String messageInitiaterId, HttpServletResponse response, int smsCode, int expectedSmsCode) throws IOException {
        logger.warning("Rejecting registration for [User]:" + messageInitiaterId +
                ". [Expected smsCode]:" + expectedSmsCode + " [Received smsCode]:" + smsCode);
    }

    private void handleException(String messageInitiaterId, HttpServletResponse response, Exception e) throws IOException {
        logger.severe("Failed registration for [User]:" + messageInitiaterId +
                ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
    }

    private Map<DataKeys,Object> registerUser(RegisterRequest request) throws SQLException {
        String messageInitiaterId = request.getMessageInitiaterId();
        String deviceModel = request.getDeviceModel();
        String androidVersion = request.getAndroidVersion();
        String pushToken = request.getPushToken();
        String iOSVersion = request.getIosVersion();
        String appVersion = request.getAppVersion();
        dao.registerUser(messageInitiaterId, pushToken, deviceModel, androidVersion, iOSVersion, appVersion);
        Map<DataKeys,Object> replyData = new HashMap<>();
        logger.info("[User]:" + messageInitiaterId + " registered successfully.");
        return replyData;
    }
    //endregion

    @Override
    public String getUrl() {
        return url;
    }
}
