package com.mediacallz.server.controllers;

import com.mediacallz.server.logic.GetAppMetaLogic;
import com.mediacallz.server.model.dto.AppMetaDTO;
import com.mediacallz.server.model.request.Request;
import com.mediacallz.server.model.response.Response;
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

    private final GetAppMetaLogic getAppMetaLogic;

    @Autowired
    public GetAppRecordController(GetAppMetaLogic getAppMetaLogic) {
        this.getAppMetaLogic = getAppMetaLogic;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/GetAppMeta", method = RequestMethod.POST)
    public Response<AppMetaDTO> getAppMeta(@Valid @RequestBody Request request, HttpServletResponse response) throws IOException {
        return getAppMetaLogic.execute(request, response);
    }
}
