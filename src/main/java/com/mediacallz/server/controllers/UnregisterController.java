package com.mediacallz.server.controllers;

import com.mediacallz.server.controllers.logic.UnregisterLogic;
import com.mediacallz.server.model.request.UnRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created by Mor on 04/10/2016.
 */
@Controller
public class UnregisterController extends AbstractController {

    private final UnregisterLogic logic;

    @Autowired
    public UnregisterController(UnregisterLogic logic) {
        this.logic = logic;
    }

    @RequestMapping("/v1/UnRegister")
    public void unregister(@Valid @RequestBody UnRegisterRequest request) {
        logic.execute(request);
    }
}
