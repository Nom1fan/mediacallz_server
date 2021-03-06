package com.mediacallz.server.dao;

import com.mediacallz.server.db.dbo.*;
import com.mediacallz.server.enums.SpecialMediaType;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mor on 16/11/2015.
 */
public interface Dao {

    //region Table users
    String  TABLE_USERS = "users";
    //region Table keys
    String COL_UID = "uid";
    String COL_TOKEN = "token";
    String COL_REGISTERED_DATE = "registered_date";
    String COL_USER_STATUS = "user_status";
    String COL_UNREGISTERED_DATE = "unregistered_date";
    String COL_UNREGISTERED_COUNT = "unregistered_count";
    String COL_DEVICE_MODEL = "device_model";
    String COL_OS = "os";
    String COL_OS_VERSION = "os_version";
    String COL_APP_VERSION = "app_version";
    //endregion
    //endregion

    //region Tables media_transfers, media_calls and media_files
    String TABLE_MEDIA_TRANSFERS = "media_transfers";
    //region Table media_transfers keys
    String COL_TRANSFER_ID = "transfer_id";
    String COL_TRANSFER_SUCCESS = "transfer_success";
    String COL_TRANSFER_DATETIME = "transfer_datetime";
    String COL_DATETIME = "datetime";
    //endregion

    String TABLE_MEDIA_CALLS = "media_calls";
    //region Table media_calls keys
    String COL_CALL_ID = "call_id";
    String COL_MD5_VISUAL = "md5_visual";
    String COL_MD5_AUDIO = "md5_audio";
    //endregion

    String TABLE_MEDIA_FILES = "media_files";
    //region Table media_files keys
    String COL_CONTENT_EXTENSION = "content_ext";
    String COL_CONTENT_SIZE = "content_size";
    String COL_TRANSFER_COUNT = "transfer_count";
    String COL_CALL_COUNT = "call_count";

    //endregion

    //region Shared keys
    String COL_TYPE = "type";     // Used in media_transfers and media calls
    String COL_UID_SRC = "uid_src";  // PK in users. FK used in media transfers and media calls
    String COL_UID_DEST = "uid_dest"; // PK in users. FK used in media transfers and media calls
    String COL_MD5 = "md5";      // PK in media_files. Used in media transfers as well.
    //endregion
    //endregion

    //region Table app_meta
    String TABLE_APP_META = "app_meta";

    //region Table keys
    String COL_CURRENT_VERSION = "current_version";
    String COL_LAST_SUPPORTED_VER = "last_supported_version";
    //endregion
    //endregion

    //region Table sms_verification
    String TABLE_SMS_VERIFICATION = "sms_verification";
    //region Table keys
    // COL_UID also used here
    String COL_CODE = "code";
    //endregion
    //endregion

    //region Table contacts
    String TABLE_CONTACTS = "contacts";
    //region Table keys
    String COL_CONTACT_UID = "contact_uid";
    String COL_CONTACT_NAME = "contact_name";
    String COL_CONTACT_SOURCE = "contact_source";
    //endregion

    //endregion

    void unregisterUser(String uid, String token) throws SQLException;

    void updateUserRecord(UserDBO userRecord) throws SQLException;

    int insertMediaTransferRecord(MediaTransferDBO mediaTransferDBO, MediaFileDBO mediaFileDBO) throws SQLException;

    MediaTransferDBO getLastMediaTransferRecord(String sourceUid, SpecialMediaType specialMediaType);

    MediaTransferDBO getLastMediaTransferRecord(String sourceUid, String destUid, SpecialMediaType specialMediaType);

    List<MediaTransferDBO> getAllUserMediaTransferRecords(String uid) throws SQLException;

    UserDBO getUserRecord(String uid) throws SQLException;

    void updateMediaTransferRecord(int commId, String column, Object value) throws SQLException;

    void updateMediaTransferRecord(int commId, String[] columns, Object[] values) throws SQLException;

    void incrementColumn(String table, String col, String whereCol, String whereVal) throws SQLException;

    void updateAppRecord(double lastSupportedVersion) throws SQLException;

    AppMetaDBO getAppMetaRecord() throws SQLException;

    int insertMediaCallRecord(MediaCallDBO mediaCallDBO, List<MediaFileDBO> mediaFileDBOS) throws SQLException;

    void insertMediaFileRecord(MediaFileDBO mediaFileDBO, String countColToInc) throws SQLException;

    MediaFileDBO getMediaFileRecord(String md5) throws SQLException;

    void insertUserSmsVerificationCode(String uid, int code) throws SQLException;

    void updateUserSmsVerificationCode(String uid, int code) throws SQLException;

    int getSmsVerificationCode(String uid) throws SQLException;

    void delete(String table, String col, String condition, String val) throws SQLException;
}

