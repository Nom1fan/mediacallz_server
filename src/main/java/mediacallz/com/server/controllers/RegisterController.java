package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.SmsVerificationAccess;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.response.Response;
import mediacallz.com.server.model.request.RegisterRequest;
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

        Map<DataKeys, Object> replyData = new HashMap<>();
        int smsCode = request.getSmsCode();
        int expectedSmsCode = smsVerificationAccess.getSmsVerificationCode(messageInitiaterId);

        if (smsCode != SmsVerificationAccess.NO_SMS_CODE && smsCode == expectedSmsCode) {
            try {
                replyData = registerUser(request);
            } catch (SQLException e) {
                handleException(messageInitiaterId, replyData, e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch(DuplicateKeyException e) {
                handleException(messageInitiaterId, replyData, e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            registrationRejected(messageInitiaterId, replyData, smsCode, expectedSmsCode);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        return new Response<>(ClientActionType.REGISTER_RES, replyData);
    }

    //region Assisting methods
    private void registrationRejected(String messageInitiaterId, Map<DataKeys, Object> replyData, int smsCode, int expectedSmsCode) {
        logger.warning("Rejecting registration for [User]:" + messageInitiaterId +
                ". [Expected smsCode]:" + expectedSmsCode + " [Received smsCode]:" + smsCode);
        replyData.put(DataKeys.IS_REGISTER_SUCCESS, false);
    }

    private void handleException(String messageInitiaterId, Map<DataKeys, Object> replyData, Exception e) {
        logger.severe("Failed registration for [User]:" + messageInitiaterId +
                ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
        replyData.put(DataKeys.IS_REGISTER_SUCCESS, false);
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
        replyData.put(DataKeys.IS_REGISTER_SUCCESS, true);
        logger.info("[User]:" + messageInitiaterId + " registered successfully.");
        return replyData;
    }
    //endregion

    @Override
    public String getUrl() {
        return url;
    }
}
