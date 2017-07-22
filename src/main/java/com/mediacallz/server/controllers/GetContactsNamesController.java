package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.GetContactsNamesLogic;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.model.request.GetContactsRequest;
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
public class GetContactsNamesController extends AbstractController {

    private final GetContactsNamesLogic logic;

    @Autowired
    public GetContactsNamesController(GetContactsNamesLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/GetContactsNames", method = RequestMethod.POST)
    public Response<List<ContactDTO>> getContacts(@Valid @RequestBody GetContactsRequest request) throws IOException {
        return logic.execute(request);
    }
}
