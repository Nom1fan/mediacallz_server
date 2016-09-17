package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.SmsVerificationAccess;
import com.mediacallz.server.model.ClientActionType;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.MessageToClient;
import com.mediacallz.server.model.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    public MessageToClient register(HttpServletRequest request) throws IOException {

        String messageInitiaterId = request.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
        logger.info("[User]:" + messageInitiaterId + " is attempting to register.");

        Map<DataKeys, Object> replyData = new HashMap<>();
        int smsCode = Integer.valueOf(request.getParameter(DataKeys.SMS_CODE.toString()));
        String pushToken = request.getParameter(DataKeys.PUSH_TOKEN.toString());
        int expectedSmsCode = smsVerificationAccess.getSmsVerificationCode(messageInitiaterId);

        if (smsCode != SmsVerificationAccess.NO_SMS_CODE && smsCode == expectedSmsCode) {
            try {
                registerUser(request, messageInitiaterId, replyData, pushToken);
            } catch (SQLException e) {
                handleSQLException(messageInitiaterId, replyData, e);
            }
        } else {
            registrationRejected(messageInitiaterId, replyData, smsCode, expectedSmsCode);
        }

        return new MessageToClient<>(ClientActionType.REGISTER_RES, replyData);
    }

    //region Assisting methods
    private void registrationRejected(String messageInitiaterId, Map<DataKeys, Object> replyData, int smsCode, int expectedSmsCode) {
        logger.warning("Rejecting registration for [User]:" + messageInitiaterId +
                ". [Expected smsCode]:" + expectedSmsCode + " [Received smsCode]:" + smsCode);
        replyData.put(DataKeys.IS_REGISTER_SUCCESS, false);
        replyData.put(DataKeys.RESPONSE_CODE, ResponseCodes.CREDENTIALS_ERR);
    }

    private void handleSQLException(String messageInitiaterId, Map<DataKeys, Object> replyData, SQLException e) {
        logger.severe("Failed registration for [User]:" + messageInitiaterId +
                ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
        replyData.put(DataKeys.IS_REGISTER_SUCCESS, false);
        replyData.put(DataKeys.RESPONSE_CODE, ResponseCodes.INTERNAL_SERVER_ERR);
    }

    private void registerUser(HttpServletRequest request, String messageInitiaterId, Map<DataKeys, Object> replyData, String pushToken) throws SQLException {
        String deviceModel = request.getParameter(DataKeys.DEVICE_MODEL.toString());
        String androidVersion = request.getParameter(DataKeys.ANDROID_VERSION.toString());
        dao.registerUser(messageInitiaterId, pushToken, deviceModel, androidVersion);
        dao.registerUser(messageInitiaterId, pushToken);
        replyData.put(DataKeys.IS_REGISTER_SUCCESS, true);
        logger.info("[User]:" + messageInitiaterId + " registered successfully.");
    }
    //endregion

    @Override
    public String getUrl() {
        return url;
    }
}
