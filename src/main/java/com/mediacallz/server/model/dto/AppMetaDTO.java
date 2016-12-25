package com.mediacallz.server.model.dto;

import com.mediacallz.server.database.dbo.AppMetaDBO;
import lombok.Data;
import ma.glasnost.orika.MapperFacade;

/**
 * Created by Mor on 20/12/2016.
 */
@Data
public class AppMetaDTO extends DTOEntity<AppMetaDBO> {

    private Double lastSupportedAppVersion;
}
