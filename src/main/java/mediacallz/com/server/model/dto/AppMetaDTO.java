package mediacallz.com.server.model.dto;

import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import mediacallz.com.server.database.dbo.AppMetaDBO;

/**
 * Created by Mor on 20/12/2016.
 */
@Data
public class AppMetaDTO implements DTO<AppMetaDBO> {

    private Double lastSupportedAppVersion;

    @Override
    public AppMetaDBO toDBO(MapperFacade mapperFacade) {
        return mapperFacade.map(this, AppMetaDBO.class);
    }
}
