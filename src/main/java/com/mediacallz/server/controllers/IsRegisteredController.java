package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.logic.IsRegisteredLogic;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.IsRegisteredRequest;
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
import java.sql.SQLException;

/**
 * Created by Mor on 24/08/2016.
 */
@Controller
public class IsRegisteredController extends AbstractController {

    private final IsRegisteredLogic logic;

    @Autowired
    public IsRegisteredController(IsRegisteredLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/IsRegistered", method = RequestMethod.POST)
    public Response<UserDTO> isRegistered(@Valid @RequestBody IsRegisteredRequest request, HttpServletResponse response) {
        return logic.execute(request, response);
    }
}
