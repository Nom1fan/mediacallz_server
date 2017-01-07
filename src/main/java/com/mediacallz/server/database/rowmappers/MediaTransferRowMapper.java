package com.mediacallz.server.database.rowmappers;

import com.mediacallz.server.database.dbo.MediaTransferDBO;
import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.database.Dao;
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
        MediaTransferDBO mediaTransferDBO = new MediaTransferDBO();
        mediaTransferDBO.setTransfer_id(resultSet.getInt(COL_TRANSFER_ID));
        mediaTransferDBO.setType(SpecialMediaType.valueOf(resultSet.getString(COL_TYPE)));
        mediaTransferDBO.setMd5(resultSet.getString(COL_MD5));
        mediaTransferDBO.setUid_src(resultSet.getString(COL_UID_SRC));
        mediaTransferDBO.setUid_dest(resultSet.getString(COL_UID_DEST));
        mediaTransferDBO.setDatetime(resultSet.getDate(COL_DATETIME));
        mediaTransferDBO.setTransfer_success(resultSet.getInt(COL_TRANSFER_SUCCESS) == 1);
        mediaTransferDBO.setTransfer_datetime( resultSet.getDate(Dao.COL_TRANSFER_DATETIME));
        return mediaTransferDBO;
    }
}
