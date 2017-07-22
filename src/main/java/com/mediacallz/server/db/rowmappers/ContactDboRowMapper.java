package com.mediacallz.server.db.rowmappers;

import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.model.dto.ContactDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mediacallz.server.dao.Dao.*;

/**
 * Created by Mor on 27/09/2016.
 */
public class ContactDboRowMapper implements RowMapper<ContactDBO> {
    @Override
    public ContactDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ContactDBO(resultSet.getString(COL_CONTACT_UID),
                            resultSet.getString(COL_CONTACT_NAME),
                            resultSet.getString(COL_CONTACT_SOURCE));
    }
}
