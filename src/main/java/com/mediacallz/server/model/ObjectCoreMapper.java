package com.mediacallz.server.model;

import com.mediacallz.server.database.dbo.AppMetaDBO;
import com.mediacallz.server.model.dto.AppMetaDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
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
