package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.MediaCallDBO;
import com.mediacallz.server.database.dbo.MediaFileDBO;
import com.mediacallz.server.logic.InsertMediaCallRecordLogic;
import com.mediacallz.server.model.EventReport;
import com.mediacallz.server.model.EventType;
import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.model.dto.MediaCallDTO;
import com.mediacallz.server.model.request.InsertMediaCallRecordRequest;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

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
    public Response insertMediaCallRecord(@Valid @RequestBody InsertMediaCallRecordRequest request, HttpServletResponse response) {
        return logic.execute(request, response);
    }
}
