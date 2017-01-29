package com.mediacallz.server.controllers;

import com.mediacallz.server.logic.UpdateUserRecordLogic;
import com.mediacallz.server.model.request.UpdateUserRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Mor on 04/10/2016.
 */
@Controller
public class UpdateUserRecordController extends AbstractController {

    private final UpdateUserRecordLogic logic;

    @Autowired
    public UpdateUserRecordController(UpdateUserRecordLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping("/v1/UpdateUserRecord")
    public void updateUserRecord(@Valid @RequestBody UpdateUserRecordRequest request, HttpServletResponse response) {
        logic.execute(request, response);
    }
}
