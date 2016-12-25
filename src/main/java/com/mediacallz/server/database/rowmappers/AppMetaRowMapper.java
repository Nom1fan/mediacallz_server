package com.mediacallz.server.database.rowmappers;

import com.mediacallz.server.database.dbo.AppMetaDBO;
import com.mediacallz.server.database.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mor on 27/09/2016.
 */
public class AppMetaRowMapper implements org.springframework.jdbc.core.RowMapper<AppMetaDBO> {
    @Override
    public AppMetaDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new AppMetaDBO(resultSet.getDouble(Dao.COL_LAST_SUPPORTED_VER));
    }
}
