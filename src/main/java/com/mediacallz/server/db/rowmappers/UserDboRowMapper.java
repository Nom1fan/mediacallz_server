package com.mediacallz.server.db.rowmappers;

import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.db.dbo.UserDBO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mediacallz.server.dao.Dao.*;

/**
 * Created by Mor on 27/09/2016.
 */
public class UserDboRowMapper implements RowMapper<UserDBO> {
    @Override
    public UserDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserDBO(resultSet.getString(COL_UID),
                            resultSet.getString(COL_TOKEN),
                            resultSet.getDate(COL_REGISTERED_DATE),
                            UserStatus.valueOf(resultSet.getString(COL_USER_STATUS)),
                            resultSet.getDate(COL_UNREGISTERED_DATE),
                            resultSet.getInt(COL_UNREGISTERED_COUNT),
                            resultSet.getString(COL_DEVICE_MODEL),
                            resultSet.getString(COL_OS),
                            resultSet.getString(COL_OS_VERSION),
                            resultSet.getString(COL_APP_VERSION));
    }
}
