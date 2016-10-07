package com.mediacallz.server.filters;

import com.mediacallz.server.controllers.PreRegistrationController;
import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.model.DataKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mor on 24/08/2016.
 */
@Component
public class VerifyUserRegisteredFilter implements Filter {

    private List<String> preRegistrationUrls;

    @Autowired
    Logger logger;

    @Autowired
    private UsersDataAccess usersDataAccess;

    @Autowired
    public void initPreRegistrationList(List<PreRegistrationController> preRegistrationControllers) {
        preRegistrationUrls = new ArrayList<>(preRegistrationControllers.size());
        for (PreRegistrationController preRegistrationController : preRegistrationControllers) {
            preRegistrationUrls.add(preRegistrationController.getUrl());
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            boolean verificationOK = true;
            String url = ((HttpServletRequest) servletRequest).getRequestURL().toString();

            String messageInitiaterId = servletRequest.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
            if (messageInitiaterId == null) {
                verificationOK = false;
                sendForbiddenError((HttpServletResponse) servletResponse, url, ((HttpServletRequest) servletRequest).getRemoteUser());
            }
            else if (!isPreRegistrationRequest(url)) { // The request requires the user to already be registered
                    boolean isRegistered = usersDataAccess.isRegistered(messageInitiaterId);
                    if (!isRegistered) {
                        verificationOK = false;
                        sendForbiddenError((HttpServletResponse) servletResponse, url, messageInitiaterId);
                    }
            }

            if (verificationOK)
                filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void sendForbiddenError(HttpServletResponse servletResponse, String url, String messageInitiaterId) throws IOException {
        logger.warning("User " + messageInitiaterId + " attempted a request in url:" + url + " but is unregistered. Request was blocked.");
        servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    private boolean isPreRegistrationRequest(String url) {
        boolean result = false;
        for (String preRegistrationUrl : preRegistrationUrls) {
            if(url.contains(preRegistrationUrl)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void destroy() {

    }
}
