package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.exceptions.DownloadRequestFailedException;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.logic.DownloadLogic;
import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.model.push.PendingDownloadData;
import com.mediacallz.server.model.request.DownloadFileRequest;
import com.mediacallz.server.services.PushSender;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.sql.SQLException;

/**
 * Created by Mor on 20/09/2016.
 */
@Controller
public class DownloadController extends AbstractController {

    private final DownloadLogic downloadLogic;

    @Autowired
    public DownloadController(DownloadLogic downloadLogic) {
        this.downloadLogic = downloadLogic;
    }

    @RequestMapping(value = "/v1/DownloadFile", method = RequestMethod.POST)
    public void downloadFile(@Valid @RequestBody DownloadFileRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        downloadLogic.execute(request, response, servletRequest);
    }
}
