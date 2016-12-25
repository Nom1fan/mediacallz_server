package com.mediacallz.server.model.dto;

import ma.glasnost.orika.MapperFacade;

/**
 * Created by Mor on 20/12/2016.
 */
public interface DTO<DBO> {

    void fromInternal(DBO internal, MapperFacade mapperFacade);

    DBO toInternal(MapperFacade mapperFacade);
}
