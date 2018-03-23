package com.mediacallz.server.controllers;

import com.google.gson.Gson;
import com.mediacallz.server.controllers.logic.UploadLogic;
import com.mediacallz.server.controllers.logic.UploadLogicV2;
import com.mediacallz.server.model.request.Request;
import com.mediacallz.server.model.request.UploadFileRequest;
import com.mediacallz.server.model.response.Response;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 12/09/2016.
 */
@Controller
public class UploadController extends AbstractController {

    private final Gson gson;

    private final UploadLogic logic;

    private final UploadLogicV2 logicV2;

    @Autowired
    public UploadController(Gson gson, UploadLogic logic, UploadLogicV2 logicV2) {
        this.gson = gson;
        this.logic = logic;
        this.logicV2 = logicV2;
    }

    @RequestMapping(value = "/v1/UploadFile", method = RequestMethod.POST)
    public void uploadFile(
            @NotNull @RequestParam("fileForUpload") MultipartFile fileForUpload,
            @NotBlank @RequestParam("jsonPart") String requestString,
            HttpServletResponse response) {

        UploadFileRequest request = gson.fromJson(requestString, UploadFileRequest.class);
        logic.execute(fileForUpload, request, response);
    }

    @ResponseBody
    @RequestMapping(value = "/v2/UploadFile", method = RequestMethod.POST)
    public Response<String> uploadFileV2(
            @NotNull @RequestParam("fileForUpload") MultipartFile fileForUpload,
            @NotBlank @RequestParam("jsonPart") String requestString,
            HttpServletResponse response) {

        Request request = gson.fromJson(requestString, Request.class);
        return logicV2.execute(request.getUser().getUid(), fileForUpload, response);
    }
}
