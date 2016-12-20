package mediacallz.com.server.model;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import mediacallz.com.server.database.dbo.AppMetaDBO;
import mediacallz.com.server.model.dto.AppMetaDTO;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 20/12/2016.
 */
@Component
public class ObjectCoreMapper extends ConfigurableMapper {

    protected void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(AppMetaDBO.class, AppMetaDTO.class)
                .field("last_supported_version", "lastSupportedAppVersion")
                .byDefault()
                .register();
    }




}
