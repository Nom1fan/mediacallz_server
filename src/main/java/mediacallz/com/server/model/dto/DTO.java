package mediacallz.com.server.model.dto;

import ma.glasnost.orika.MapperFacade;

/**
 * Created by Mor on 20/12/2016.
 */
public interface DTO<DBO> {
    DBO toDBO(MapperFacade mapperFacade);
}
