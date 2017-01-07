package com.mediacallz.server.controllers;

import com.google.gson.Gson;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.lang.StringsFactory;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by Mor on 20/08/2016.
 */
@NoArgsConstructor
public abstract class AbstractController {

    @Autowired
    protected Logger logger;

    @Autowired
    protected StringsFactory stringsFactory;

    protected void sendResponse(HttpServletResponse servletResponse, Response response, int status) throws IOException {
        PrintWriter responseWriter = servletResponse.getWriter();
        servletResponse.setStatus(status);
        responseWriter.write(new Gson().toJson(response));
        responseWriter.flush();
        responseWriter.close();
    }

}
