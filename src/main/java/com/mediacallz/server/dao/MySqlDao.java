package com.mediacallz.server.dao;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.db.dbo.*;
import com.mediacallz.server.db.rowmappers.*;
import com.mediacallz.server.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mor on 19/12/2015.
 */
@Repository
public class MySqlDao implements Dao {

    private final Logger logger;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MySqlDao(Logger logger, JdbcTemplate jdbcTemplate) {
        this.logger = logger;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void unregisterUser(String uid, String token) throws SQLException {

        StringBuilder query = new StringBuilder();
        token = quote(token);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sNow = quote(sdf.format(new Date()));
        String userStatus = quote(UserStatus.UNREGISTERED.toString());

        query.
                append("UPDATE ").
                append(TABLE_USERS).
                append(" SET ").
                append(COL_USER_STATUS).
                append("=").
                append(userStatus).append(",").
                append(COL_UNREGISTERED_DATE).
                append("=").
                append(sNow).
                append(" WHERE ").
                append(COL_UID).
                append("=").
                append(quote(uid)).
                append(" AND ").
                append(COL_TOKEN).
                append("=").
                append(token);

        executeQuery(query.toString());

        incrementColumn(TABLE_USERS, COL_UNREGISTERED_COUNT, COL_UID, uid);
    }

    @Override
    public UserDBO getUserRecord(String uid) throws SQLException {
        UserDBO result = null;
        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_UID + "=" + quote(uid);
            logger.config("Executing SQL query:[" + query + "]");
            result = jdbcTemplate.queryForObject(query, new UserDboRowMapper());
        } catch (EmptyResultDataAccessException ignored) {
        }
        return result;
    }

    @Override
    public AppMetaDBO getAppMetaRecord() throws SQLException {
        String query = "SELECT * FROM " + TABLE_APP_META;
        return jdbcTemplate.queryForObject(query, new AppMetaRowMapper());
    }

    //TODO Mor: Test this method
    @Override
    public void updateMediaTransferRecord(int commId, String[] columns, Object[] values) throws SQLException {

        if (columns.length < 1)
            return;

        StringBuilder query = new StringBuilder();
        query.append("UPDATE " + TABLE_MEDIA_TRANSFERS + " SET " + columns[0] + "=" + "\"" + values[0] + "\"");
        for (int i = 1; i < columns.length; ++i) {
            query.append(", ");
            query.append(columns[i] + "=" + "\"" + values[i] + "\"");
        }

        query.append(" WHERE " + COL_TRANSFER_ID + "=" + "\"" + commId + "\"");
        executeQuery(query.toString());
    }

    @Override
    public void updateMediaTransferRecord(int commId, String column, Object value) throws SQLException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sCommId = quote(String.valueOf(commId));
        String date = quote(sdf.format(new Date()));
        value = quote((String.valueOf(value)));

        StringBuilder query = new StringBuilder();

        query.
                append("UPDATE ").
                append(TABLE_MEDIA_TRANSFERS).
                append(" SET ").
                append(column).append("=").
                append(value).append(", ").
                append(COL_TRANSFER_DATETIME).append("=").
                append(date).
                append(" WHERE ").
                append(COL_TRANSFER_ID).append('=').
                append(sCommId);

        executeQuery(query.toString());
    }

    @Override
    public void updateUserRecord(UserDBO userRecord) throws SQLException {

        StringBuilder query = new StringBuilder("UPDATE " + TABLE_USERS).append(" SET ");
        String uid = quote(userRecord.getUid());
        String osVersion = userRecord.getOsVersion() == null ? "NULL" : quote(userRecord.getOsVersion());
        String os = userRecord.getOs() == null ? "NULL" : quote(userRecord.getOs());
        String appVersion = userRecord.getAppVersion() == null ? "NULL" : quote(userRecord.getAppVersion());

        query.
                append(COL_OS_VERSION).
                append("=").
                append(osVersion).
                append(",").
                append(COL_OS).
                append("=").
                append(os).
                append(",").
                append(COL_APP_VERSION).
                append("=").
                append(appVersion).
                append(" WHERE ").
                append(COL_UID).
                append("=").
                append(uid);

        executeQuery(query.toString());
    }

    @Override
    public void updateUserSmsVerificationCode(String uid, int code) throws SQLException {

        String query = "UPDATE " + TABLE_SMS_VERIFICATION + " SET " + COL_CODE + "=" + code + " WHERE " + COL_UID + "=" + quote(uid);
        executeQuery(query);
    }

    @Override
    public void updateAppRecord(double lastSupportedVersion) throws SQLException {

        //TODO Solve bug currentVersion as index
        StringBuilder query = new StringBuilder();

        query.
                append("UPDATE ").append(TABLE_APP_META).
                append(" SET ").
                append(COL_LAST_SUPPORTED_VER).
                append("=").
                append(lastSupportedVersion).
                append(" WHERE ").
                append(COL_LAST_SUPPORTED_VER).
                append(" > 0");

        executeQuery(query.toString());
    }

    @Override
    public int insertMediaTransferRecord(MediaTransferDBO mediaTransferDBO, MediaFileDBO mediaFileDBO) throws SQLException {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).
                withTableName(TABLE_MEDIA_TRANSFERS).
                usingGeneratedKeyColumns(COL_TRANSFER_ID);

        insertMediaFileRecord(mediaFileDBO, COL_TRANSFER_COUNT);

        SqlParameterSource parameters = new BeanPropertySqlParameterSource(mediaTransferDBO);
        return jdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public int insertMediaCallRecord(MediaCallDBO mediaCallDBO, List<MediaFileDBO> mediaFileDBOS) throws SQLException {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).
                withTableName(TABLE_MEDIA_CALLS).
                usingGeneratedKeyColumns(COL_CALL_ID);

        for (MediaFileDBO mediaFileDBO : mediaFileDBOS) {
            if (mediaFileDBO != null) {
                insertMediaFileRecord(mediaFileDBO, COL_TRANSFER_COUNT);
            }
        }

        SqlParameterSource parameters = new BeanPropertySqlParameterSource(mediaCallDBO);
        return jdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public void insertMediaFileRecord(MediaFileDBO mediaFileDBO, String countColToInc) throws SQLException {

        if (getMediaFileRecord(mediaFileDBO.getMd5()) != null) {
            incrementColumn(TABLE_MEDIA_FILES, countColToInc, COL_MD5, mediaFileDBO.getMd5());
            return;
        }
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).
                withTableName(TABLE_MEDIA_FILES);

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(mediaFileDBO);
        jdbcInsert.execute(parameterSource);
        incrementColumn(TABLE_MEDIA_FILES, countColToInc, COL_MD5, mediaFileDBO.getMd5());
    }

    @Override
    public MediaFileDBO getMediaFileRecord(String md5) throws SQLException {
        MediaFileDBO result = null;
        try {
            String query = "SELECT * FROM " + TABLE_MEDIA_FILES + " WHERE " + COL_MD5 + "=" + quote(md5);
            result = jdbcTemplate.queryForObject(query, new MediaFileRowMapper());
        } catch (EmptyResultDataAccessException ignored) {
        }
        return result;
    }

    @Override
    public void insertUserSmsVerificationCode(String uid, int code) throws SQLException {

        StringBuilder query = new StringBuilder();
        uid = quote(uid);

        query.
                append("INSERT INTO ").
                append(TABLE_SMS_VERIFICATION).
                append(" (").
                append(COL_UID).append(",").
                append(COL_CODE).
                append(")").
                append(" VALUES (").
                append(uid).append(",").
                append(code).
                append(")");

        executeQuery(query.toString());
    }

    @Override
    public int getSmsVerificationCode(String uid) throws SQLException {
        int smsCode = 0;
        try {
            String query = "SELECT * FROM " + TABLE_SMS_VERIFICATION + " WHERE " + COL_UID + "=" + quote(uid);
            SmsVerificationDBO smsVerificationDBO = jdbcTemplate.queryForObject(query, new SmsVerificationRowMapper());
            smsCode = smsVerificationDBO.getCode();
        } catch (EmptyResultDataAccessException ignored) {
        }
        return smsCode;
    }

    @Override
    public List<MediaTransferDBO> getAllUserMediaTransferRecords(String uid) throws SQLException {
        String query = "SELECT *" + " FROM " + TABLE_MEDIA_TRANSFERS + " WHERE " + COL_UID_SRC + "=?";
        return jdbcTemplate.query(query, new MediaTransferRowMapper(), uid);
    }

    @Override
    public void incrementColumn(String table, String col, String whereCol, String whereVal) throws SQLException {

        StringBuilder query = new StringBuilder();

        query.
                append("UPDATE ").
                append(table).
                append(" SET ").
                append(col).
                append("=").
                append(col).append(" + 1").
                append(" WHERE ").
                append(whereCol).
                append("=").
                append(quote(whereVal));

        executeQuery(query.toString());
    }

    @Override
    public void delete(String table, String col, String condition, String val) throws SQLException {
        val = quote(val);
        String query = "DELETE FROM " + table + " WHERE " + col + condition + val;
        executeQuery(query);
    }
    //endregion

    private void executeQuery(String query) throws SQLException {
        logger.config("Executing SQL query:[" + query + "]");
        jdbcTemplate.execute(query);
    }

    private String quote(String str) {
        return "\"" + str + "\"";
    }
    //endregion
}
