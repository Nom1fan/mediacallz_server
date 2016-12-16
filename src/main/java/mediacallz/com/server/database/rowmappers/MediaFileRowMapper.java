package mediacallz.com.server.database.rowmappers;

import mediacallz.com.server.database.dbos.MediaFileDBO;
import mediacallz.com.server.database.Dao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mor on 27/09/2016.
 */
public class MediaFileRowMapper implements RowMapper<MediaFileDBO> {
    @Override
    public MediaFileDBO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new MediaFileDBO(resultSet.getString(Dao.COL_MD5), resultSet.getString(Dao.COL_CONTENT_EXTENSION), resultSet.getLong(Dao.COL_CONTENT_SIZE));
    }
}
