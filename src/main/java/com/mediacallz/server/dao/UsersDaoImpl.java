package com.mediacallz.server.dao;

import com.mchange.v2.c3p0.impl.NewProxyCallableStatement;
import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.db.dbo.MediaTransferDBO;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.db.rowmappers.ContactDboRowMapper;
import com.mediacallz.server.db.rowmappers.UserDboRowMapper;
import com.mediacallz.server.db.rowmappers.UserDboUserStatusRowMapper;
import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.model.push.ClearMediaData;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.services.PushSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mediacallz.server.dao.Dao.*;


/**
 * This class is manages all user-related DAL access
 *
 * @author Mor
 */
@Component
@Slf4j
@Transactional
public class UsersDaoImpl implements UsersDao {

    private final PushSender pushSender;

    private final Dao dao;
    
    private final NamedParameterJdbcOperations jdbcOperations;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersDaoImpl(PushSender pushSender,
                        Dao dao,
                        @Qualifier("myNamedParameterJdbcOperations")
                        NamedParameterJdbcOperations jdbcOperations,
                        JdbcTemplate jdbcTemplate) {
        this.pushSender = pushSender;
        this.dao = dao;
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean registerUser(UserDBO user) {
        if (getUserRecord(user.getUid()) == null) {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource());
            jdbcInsert.withTableName(TABLE_USERS);
            user.setRegistered_date(new Date());
            user.setUserStatus(UserStatus.REGISTERED);
            BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(user);
            int numRowsAffected = jdbcInsert.execute(parameters);
            return numRowsAffected != 0;
        }
        return updateUser(user);
    }

    @Override
    public boolean updateUser(UserDBO user) {
        String param = "=?,";
        String lastParam = "=?";
        String strquery = "UPDATE " +
                TABLE_USERS + " SET " +
                COL_TOKEN + param +
                COL_REGISTERED_DATE + param +
                COL_USER_STATUS + param +
                COL_DEVICE_MODEL + param +
                COL_OS + param +
                COL_OS_VERSION + param +
                COL_APP_VERSION + lastParam +
                " WHERE " + COL_UID + lastParam;

        int numRowsUpdated = jdbcTemplate.update(
                strquery,
                user.getToken(),
                new Date(),
                UserStatus.REGISTERED.getStr(),
                user.getDeviceModel(),
                user.getOs(),
                user.getOsVersion(),
                user.getAppVersion(),
                user.getUid());

        return numRowsUpdated != 0;
    }

    @Override
    public boolean unregisterUser(String userId, String token) {

        try {
            List<MediaTransferDBO> records = dao.getAllUserMediaTransferRecords(userId);

            Set<String> destinations = new HashSet<>();

            // Creating a set of all destinations who received media from the user
            for (MediaTransferDBO record : records) {
                if (record.isTransfer_success())
                    destinations.add(record.getUid_dest());
            }

            // Clearing all media sent to these destinations by user
            for (String destination : destinations) {

                String pushEventAction = PushEventKeys.CLEAR_MEDIA;
                String destToken = dao.getUserRecord(destination).getToken();

                final SpecialMediaType[] specialMediaTypes = SpecialMediaType.values();

                // Clearing all types of special media
                for (SpecialMediaType specialMediaType : specialMediaTypes) {


                    ClearMediaData clearMediaData = new ClearMediaData();
                    clearMediaData.setSpecialMediaType(specialMediaType);
                    clearMediaData.setSourceId(userId);
                    boolean sent = pushSender.sendPush(destToken, pushEventAction, clearMediaData);
                    if (!sent)
                        log.warn("Failed to send push to clear media. [User]:" + destination + " [SpecialMediaType]:" + specialMediaType);
                }
            }

            dao.unregisterUser(userId, token);

            log.info("Unregistered [User]:" + userId + ". [Token]:" + token + " successfully");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unregistered failure. " + " [User]:" + userId +
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
                log.info("User not found. [User]:" + userId + " is " + UserStatus.UNREGISTERED.toString());
                return false;
            }

            if (record.getUserStatus().equals(UserStatus.REGISTERED)) {
                log.info("[User]:" + userId + " is " + UserStatus.REGISTERED.toString());
                return true;
            }

        } catch (Exception e) {
            log.error("isRegistered failure. " + " [User]:" + userId +
                    "[Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
            return false;
        }

        log.info("[User]:" + userId + " is " + record.getUserStatus());
        return false;
    }

    @Override
    public UserDBO getUserRecord(String uid) {
        UserDBO result = null;
        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_UID + "=" + quote(uid);
            log.debug("Executing SQL query:[" + query + "]");
            result = jdbcTemplate.queryForObject(query, new UserDboRowMapper());
        } catch (EmptyResultDataAccessException ignored) {
        }
        return result;
    }

    @Override
    public List<UserDBO> getRegisteredContacts(List<String> contactsUids) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", contactsUids);

        String query = "SELECT uid,user_status FROM sys.users WHERE uid IN (:ids) AND user_status <> '" + UserStatus.UNREGISTERED.getStr() + "'";
        return jdbcOperations.query(query, parameters, new UserDboUserStatusRowMapper());
    }

    @Override
    public boolean syncContacts(List<ContactDBO> contacts) {
        List<ContactDBO> existingContacts = getContacts(contacts.stream().map(ContactDBO::getContact_uid).collect(Collectors.toList()));
        List<ContactDBO> filteredContacts = contacts.stream().filter(contact -> !existingContacts.contains(contact)).collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(filteredContacts.toArray());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource());
        insert.withTableName(TABLE_CONTACTS);
        insert.executeBatch(batch);
        return true;
    }

    @Override
    public List<ContactDBO> getContacts(List<String> contactsUids) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", contactsUids);

        String query = "SELECT * FROM sys.contacts WHERE contacts.contact_uid IN (:ids)";

        return jdbcOperations.query(query, parameters, new ContactDboRowMapper());
    }

    private String quote(String str) {
        return "\"" + str + "\"";
    }

}
