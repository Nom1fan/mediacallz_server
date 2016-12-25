package com.mediacallz.server.database.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import com.mediacallz.server.model.dto.AppMetaDTO;

/**
 * Created by Mor on 26/03/2016.
 */
@Data
@AllArgsConstructor
public class AppMetaDBO extends DBOEntity {

    private double last_supported_version;
}
