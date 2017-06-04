package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.ClearMediaLogic;
import com.mediacallz.server.model.request.ClearMediaRequest;
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
public class ClearMediaController extends AbstractController {

    private final ClearMediaLogic clearMediaLogic;

    @Autowired
    public ClearMediaController(ClearMediaLogic clearMediaLogic) {
        this.clearMediaLogic = clearMediaLogic;
    }

    @ResponseBody
    @RequestMapping("/v1/ClearMedia")
    public void clearMedia(@Valid @RequestBody ClearMediaRequest request, HttpServletResponse response) {
        clearMediaLogic.execute(request, response);
    }
}
