package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.SmsVerificationDao;
import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
@Slf4j
public class RegisterLogic extends AbstractServerLogic {

    private final SmsVerificationDao smsVerificationDao;

    private UsersDao usersDao;

    private MapperFacade mapperFacade;

    @Autowired
    public RegisterLogic(SmsVerificationDao smsVerificationDao, UsersDao usersDao, MapperFacade mapperFacade) {
        this.smsVerificationDao = smsVerificationDao;
        this.usersDao = usersDao;
        this.mapperFacade = mapperFacade;
    }

    public void execute(RegisterRequest request, HttpServletResponse response) throws IOException {
        UserDTO user = request.getUser();
        String messageInitiaterId = user.getUid();
        log.info("[User]:" + messageInitiaterId + " is attempting to register.");

        int smsCode = request.getSmsCode();
        int expectedSmsCode = smsVerificationDao.getSmsVerificationCode(messageInitiaterId);

        if (smsCode != SmsVerificationDao.NO_SMS_CODE && smsCode == expectedSmsCode) {
            UserDBO userDBO = user.toInternal(mapperFacade);
            usersDao.registerUser(userDBO);
            log.info("[User]:" + messageInitiaterId + " registered successfully.");

        } else {
            registrationRejected(messageInitiaterId, smsCode, expectedSmsCode);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    //region Assisting methods
    private void registrationRejected(String messageInitiaterId, int smsCode, int expectedSmsCode) throws IOException {
        log.warn("Rejecting registration for [User]:" + messageInitiaterId +
                ". [Expected smsCode]:" + expectedSmsCode + " [Received smsCode]:" + smsCode);
    }

    private void handleException(String messageInitiaterId, Exception e) throws IOException {
        log.error("Failed registration for [User]:" + messageInitiaterId +
                ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
    }
    //endregion
}
