package com.mediacallz.server.controllers;

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
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class RegisterController extends PreRegistrationController {

    private final String url = "/v1/Register";

    private final SmsVerificationAccess smsVerificationAccess;

    private final Dao dao;

    @Autowired
    public RegisterController(SmsVerificationAccess smsVerificationAccess, Dao dao) {
        this.smsVerificationAccess = smsVerificationAccess;
        this.dao = dao;
    }

    @ResponseBody
    @RequestMapping(value = url, method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) throws IOException {
        String messageInitiaterId = request.getMessageInitiaterId();
        logger.info("[User]:" + messageInitiaterId + " is attempting to register.");

        int smsCode = request.getSmsCode();
        int expectedSmsCode = smsVerificationAccess.getSmsVerificationCode(messageInitiaterId);

        if (smsCode != SmsVerificationAccess.NO_SMS_CODE && smsCode == expectedSmsCode) {
            try {
                registerUser(request);
            } catch (SQLException e) {
                handleException(messageInitiaterId, e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (DuplicateKeyException e) {
                handleException(messageInitiaterId, e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            registrationRejected(messageInitiaterId, smsCode, expectedSmsCode);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    //region Assisting methods
    private void registrationRejected(String messageInitiaterId, int smsCode, int expectedSmsCode) throws IOException {
        logger.warning("Rejecting registration for [User]:" + messageInitiaterId +
                ". [Expected smsCode]:" + expectedSmsCode + " [Received smsCode]:" + smsCode);
    }

    private void handleException(String messageInitiaterId, Exception e) throws IOException {
        logger.severe("Failed registration for [User]:" + messageInitiaterId +
                ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
    }

    private void registerUser(RegisterRequest request) throws SQLException {
        String messageInitiaterId = request.getMessageInitiaterId();
        String deviceModel = request.getDeviceModel();
        String androidVersion = request.getAndroidVersion();
        String pushToken = request.getPushToken();
        String iOSVersion = request.getIosVersion();
        String appVersion = request.getAppVersion();
        dao.registerUser(messageInitiaterId, pushToken, deviceModel, androidVersion, iOSVersion, appVersion);
        logger.info("[User]:" + messageInitiaterId + " registered successfully.");
    }
    //endregion

    @Override
    public String getUrl() {
        return url;
    }
}
