package com.mediacallz.server.database.rowmappers;

import com.mediacallz.server.database.dbos.MediaTransferDBO;
import com.mediacallz.server.model.SpecialMediaType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mediacallz.server.database.Dao.*;


/**
 * Created by Mor on 27/09/2016.
 */
public class MediaTransferRowMapper implements RowMapper<MediaTransferDBO> {
    @Override
    public MediaTransferDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new MediaTransferDBO(resultSet.getInt(COL_TRANSFER_ID),
                                    SpecialMediaType.valueOf(resultSet.getString(COL_TYPE)),
                                    resultSet.getString(COL_MD5),
                                    resultSet.getString(COL_UID_SRC),
                                    resultSet.getString(COL_UID_DEST),
                                    resultSet.getDate(COL_DATETIME),
                                    resultSet.getInt(COL_TRANSFER_SUCCESS) == 1,
                                    resultSet.getDate(COL_TRANSFER_DATETIME));
    }
}