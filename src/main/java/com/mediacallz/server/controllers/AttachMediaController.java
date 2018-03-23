package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.AttachMediaLogic;
import com.mediacallz.server.model.request.AttachMediaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Mor on 12/09/2016.
 */
@Controller
public class AttachMediaController extends AbstractController {

    private final AttachMediaLogic logic;

    @Autowired
    public AttachMediaController(AttachMediaLogic logic) {
        this.logic = logic;
    }

    @RequestMapping(value = "/v1/AttachMedia", method = RequestMethod.POST)
    public void attachMedia(@Valid @RequestBody AttachMediaRequest request, HttpServletResponse response) throws IOException {
        logic.execute(request, response);
    }

}
