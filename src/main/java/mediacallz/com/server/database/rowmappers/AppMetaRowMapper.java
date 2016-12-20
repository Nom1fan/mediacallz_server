package mediacallz.com.server.database.rowmappers;

import mediacallz.com.server.database.dbo.AppMetaDBO;
import mediacallz.com.server.database.Dao;

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
