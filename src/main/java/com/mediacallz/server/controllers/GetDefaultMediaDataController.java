package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.GetDefaultMediaDataLogic;
import com.mediacallz.server.controllers.logic.GetRegisteredContactsLogic;
import com.mediacallz.server.model.dto.DefaultMediaDataContainerDTO;
import com.mediacallz.server.model.dto.DefaultMediaDataDTO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.GetDefaultMediaDataRequest;
import com.mediacallz.server.model.request.GetRegisteredContactsRequest;
import com.mediacallz.server.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mor on 1/18/2017.
 */
@Controller
public class GetDefaultMediaDataController extends AbstractController {

    private final GetDefaultMediaDataLogic logic;

    @Autowired
    public GetDefaultMediaDataController(GetDefaultMediaDataLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/GetDefaultMediaData", method = RequestMethod.POST)
    public Response<List<DefaultMediaDataContainerDTO>> getAppMeta(@Valid @RequestBody GetDefaultMediaDataRequest request) throws IOException {
        return logic.execute(request);
    }
}
