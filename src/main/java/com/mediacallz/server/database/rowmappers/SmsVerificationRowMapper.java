package com.mediacallz.server.database.rowmappers;

import com.mediacallz.server.database.dbo.SmsVerificationDBO;
import com.mediacallz.server.database.Dao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mor on 27/09/2016.
 */
public class SmsVerificationRowMapper implements RowMapper<SmsVerificationDBO> {
    @Override
    public SmsVerificationDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SmsVerificationDBO(resultSet.getString(Dao.COL_UID), resultSet.getInt(Dao.COL_CODE));
    }
}
