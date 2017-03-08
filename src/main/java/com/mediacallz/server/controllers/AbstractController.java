package com.mediacallz.server.controllers;

import com.google.gson.Gson;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.lang.StringsFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by Mor on 20/08/2016.
 */
@NoArgsConstructor
@Slf4j
public abstract class AbstractController {

    @Autowired
    protected StringsFactory stringsFactory;
}
