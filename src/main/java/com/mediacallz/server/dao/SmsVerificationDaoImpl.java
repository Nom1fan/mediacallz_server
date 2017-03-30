package com.mediacallz.server.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Created by Mor on 28/03/2016.
 */
@Component
@Slf4j
public class SmsVerificationDaoImpl implements SmsVerificationDao {

    @Autowired
    private Dao dao;

    @Override
    public boolean insertSmsVerificationCode(String uid, int code) {

        try {
            dao.delete(Dao.TABLE_SMS_VERIFICATION, Dao.COL_UID, "=", uid);
            dao.insertUserSmsVerificationCode(uid, code);

            log.info("SMS verification code inserted for [User]:" + uid + ". [Code]:" + code);
            return true;
        } catch (SQLException e) {

            e.printStackTrace();
            log.error("Failed to insert SMS verification code for [User]:" + uid + ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
            return false;
        }
    }

    @Override
    public int getSmsVerificationCode(String uid) {

        try {
            int code = dao.getSmsVerificationCode(uid);
            return code != 0 ? code : NO_SMS_CODE;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Failed to get SMS verification code for [User]:" + uid + ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
            return NO_SMS_CODE;
        }
    }
}
