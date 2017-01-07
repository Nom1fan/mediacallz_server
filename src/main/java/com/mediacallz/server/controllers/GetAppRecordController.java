package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.model.dto.AppMetaDTO;
import com.mediacallz.server.model.request.Request;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class GetAppRecordController extends AbstractController {

    private final Dao dao;

    private final MapperFacade mapperFacade;

    @Autowired
    public GetAppRecordController(MapperFacade mapperFacade, Dao dao) {
        this.mapperFacade = mapperFacade;
        this.dao = dao;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/GetAppMeta", method = RequestMethod.POST)
    public Response getAppMeta(@Valid @RequestBody Request request, HttpServletResponse response) throws IOException {

        try {
            AppMetaDTO appMetaDTO = new AppMetaDTO();
            appMetaDTO.fromInternal(dao.getAppMetaRecord(), mapperFacade);
            return new Response<>(appMetaDTO);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = "Failed to retrieve app meta from DB. " + (e.getMessage() != null ? "Exception:" + e.getMessage() : "");
            logger.severe(errMsg);
            return null;
        }
    }
}
