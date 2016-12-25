package com.mediacallz.server.model.dto;

import com.mediacallz.server.database.dbo.DBOEntity;
import ma.glasnost.orika.MapperFacade;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Mor on 12/25/2016.
 */

public abstract class DTOEntity<DBO extends DBOEntity> implements DTO<DBO> {

    @Override
    public void fromInternal(DBO internal, MapperFacade mapperFacade) {
        mapperFacade.map(internal, this);
    }

    @Override
    public DBO toInternal(MapperFacade mapperFacade) {
        Class<DBO> internalClass = (Class<DBO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return mapperFacade.map(this, internalClass);
    }
}
