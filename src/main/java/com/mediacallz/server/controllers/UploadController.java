package com.mediacallz.server.controllers;

import com.google.gson.Gson;
import com.mediacallz.server.logic.UploadLogic;
import com.mediacallz.server.model.request.UploadFileRequest;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;

/**
 * Created by Mor on 12/09/2016.
 */
@Controller
public class UploadController extends AbstractController {

    private final Gson gson;

    private final UploadLogic logic;

    @Autowired
    public UploadController(Gson gson, UploadLogic logic) {
        this.gson = gson;
        this.logic = logic;
    }

    @RequestMapping(value = "/v1/UploadFile", method = RequestMethod.POST)
    public void uploadFile(
            @NotNull @RequestParam("fileForUpload") MultipartFile fileForUpload,
            @NotBlank @RequestParam("jsonPart") String requestString,
            HttpServletResponse response) throws IOException, ServletException {

        UploadFileRequest request = gson.fromJson(requestString, UploadFileRequest.class);
        logic.execute(fileForUpload, request, response);
    }
}
