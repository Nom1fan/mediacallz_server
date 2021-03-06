package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.GetRegisteredContactsLogic;
import com.mediacallz.server.model.dto.UserDTO;
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
public class GetRegisteredContactsController extends AbstractController {

    private final GetRegisteredContactsLogic logic;

    @Autowired
    public GetRegisteredContactsController(GetRegisteredContactsLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/GetRegisteredContacts", method = RequestMethod.POST)
    public Response<List<UserDTO>> getRegisteredContacts(@Valid @RequestBody GetRegisteredContactsRequest request) throws IOException {
        return logic.execute(request);
    }
}
