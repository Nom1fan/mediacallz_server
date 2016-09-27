package com.mediacallz.server.database;

import com.mediacallz.server.database.dbos.MediaTransferDBO;
import com.mediacallz.server.database.dbos.UserDBO;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.model.UserStatus;
import com.mediacallz.server.services.PushSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is a singleton that manages all user-related DAL access
 *
 * @author Mor
 */
@Component
public class UsersDataAccessImpl implements UserDataAccess {

    @Autowired
    private PushSender pushSender;

    @Autowired
    private Dao dao;
    
    @Autowired
    private Logger logger;

    @Override
    public boolean unregisterUser(String userId, String token) {

        try {
            List<MediaTransferDBO> records = dao.getAllUserMediaTransferRecords(userId);

            Set<String> destinations = new HashSet<>();

            // Creating a set of all destinations who received media from the user
            for(MediaTransferDBO record : records) {
                if(record.isTransfer_success())
                    destinations.add(record.getUid_dest());
            }

            // Clearing all media sent to these destinations by user
            for(String destination : destinations) {

                String pushEventAction = PushEventKeys.CLEAR_MEDIA;
                String destToken = dao.getUserRecord(destination).getToken();

                final SpecialMediaType[] specialMediaTypes = SpecialMediaType.values();

                // Clearing all types of special media
                for(SpecialMediaType specialMediaType : specialMediaTypes) {

                    HashMap<DataKeys,Object> data = new HashMap();
                    data.put(DataKeys.SPECIAL_MEDIA_TYPE, specialMediaType);
                    data.put(DataKeys.SOURCE_ID, userId);
                    boolean sent = pushSender.sendPush(destToken, pushEventAction, data);
                    if (!sent)
                        logger.warning("Failed to send push to clear media. [User]:" + destination + " [SpecialMediaType]:" + specialMediaType);
                }
            }

            dao.unregisterUser(userId, token);

            logger.info("Unregistered [User]:" + userId + ". [Token]:" + token + " successfully");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Unregistered failure. " + " [User]:" + userId +
                    "[Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
            return false;
        }
    }

    @Override
    public boolean isRegistered(String userId) {

        UserDBO record;
        try {

            record = dao.getUserRecord(userId);

            if (record == null) {

                logger.info("[User]:" + userId + " is " + UserStatus.UNREGISTERED.toString());
                return false;
            }

            if (record.getUserStatus().equals(UserStatus.REGISTERED)) {

                logger.info("[User]:" + userId + " is " + UserStatus.REGISTERED.toString());
                return true;
            }

        } catch (Exception e) {
            logger.severe("isRegistered failure. " + " [User]:" + userId +
                    "[Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
            return false;
        }

        logger.info("[User]:" + userId + " is " + record.getUserStatus());
        return false;
    }

    @Override
    public UserDBO getUserRecord(String destId) {
        UserDBO result = null;

        try {
            result = dao.getUserRecord(destId);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

}
