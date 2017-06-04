package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.InsertMediaCallRecordLogic;
import com.mediacallz.server.model.request.InsertMediaCallRecordRequest;
import com.mediacallz.server.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Mor on 03/10/2016.
 */
@Controller
public class InsertMediaCallRecordController extends AbstractController {

    private final InsertMediaCallRecordLogic logic;

    @Autowired
    public InsertMediaCallRecordController(InsertMediaCallRecordLogic logic) {
        this.logic = logic;
    }

    @ResponseBody
    @RequestMapping("/v1/InsertMediaCallRecord")
    public Response<Integer> insertMediaCallRecord(@Valid @RequestBody InsertMediaCallRecordRequest request, HttpServletResponse response) {
        return logic.execute(request, response);
    }
}
