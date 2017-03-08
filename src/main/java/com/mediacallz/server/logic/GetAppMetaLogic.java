package com.mediacallz.server.logic;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.model.dto.AppMetaDTO;
import com.mediacallz.server.model.request.Request;
import com.mediacallz.server.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
@Slf4j
public class GetAppMetaLogic extends AbstractServerLogic {

    private final Dao dao;

    private final MapperFacade mapperFacade;

    @Autowired
    public GetAppMetaLogic(MapperFacade mapperFacade, Dao dao) {
        this.mapperFacade = mapperFacade;
        this.dao = dao;
    }

    public Response<AppMetaDTO> execute(Request request, HttpServletResponse response) {
        try {
            AppMetaDTO appMetaDTO = new AppMetaDTO();
            appMetaDTO.fromInternal(dao.getAppMetaRecord(), mapperFacade);
            return new Response<>(appMetaDTO);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = "Failed to retrieve app meta from DB. " + (e.getMessage() != null ? "Exception:" + e.getMessage() : "");
            log.error(errMsg);
            return null;
        }
    }
}
