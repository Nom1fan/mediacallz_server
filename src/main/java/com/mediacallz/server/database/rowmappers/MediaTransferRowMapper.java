package com.mediacallz.server.database.rowmappers;

import com.mediacallz.server.database.dbo.MediaTransferDBO;
import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.database.Dao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Mor on 27/09/2016.
 */
public class MediaTransferRowMapper implements RowMapper<MediaTransferDBO> {
    @Override
    public MediaTransferDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new MediaTransferDBO(resultSet.getInt(Dao.COL_TRANSFER_ID),
                                    SpecialMediaType.valueOf(resultSet.getString(Dao.COL_TYPE)),
                                    resultSet.getString(Dao.COL_MD5),
                                    resultSet.getString(Dao.COL_UID_SRC),
                                    resultSet.getString(Dao.COL_UID_DEST),
                                    resultSet.getDate(Dao.COL_DATETIME),
                                    resultSet.getInt(Dao.COL_TRANSFER_SUCCESS) == 1,
                                    resultSet.getDate(Dao.COL_TRANSFER_DATETIME));
    }
}
