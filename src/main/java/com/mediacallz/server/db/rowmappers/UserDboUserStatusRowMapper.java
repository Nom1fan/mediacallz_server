package com.mediacallz.server.db.rowmappers;

import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.enums.UserStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mediacallz.server.dao.Dao.*;

/**
 * Created by Mor on 27/09/2016.
 */
public class UserDboUserStatusRowMapper implements RowMapper<UserDBO> {
    @Override
    public UserDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserDBO(resultSet.getString(COL_UID),
                            null,
                            null,
                            UserStatus.valueOf(resultSet.getString(COL_USER_STATUS)),
                            null,
                            0,
                            null,
                            null,
                            null,
                            null);
    }
}
