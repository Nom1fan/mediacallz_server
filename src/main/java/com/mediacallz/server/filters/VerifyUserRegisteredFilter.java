package com.mediacallz.server.filters;

import com.mediacallz.server.controllers.PreRegistrationController;
import com.mediacallz.server.database.UserDataAccess;
import com.mediacallz.server.model.DataKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mor on 24/08/2016.
 */
@Component
public class VerifyUserRegisteredFilter implements Filter {

    private List<PreRegistrationController> preRegistrationControllers;

    @Autowired
    Logger logger;

    @Autowired
    private UserDataAccess usersDataAccess;

    @Autowired
    public void initPreRegistrationList(List<PreRegistrationController> preRegistrationControllers) {
        this.preRegistrationControllers = preRegistrationControllers;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            boolean verificationOK = true;
            String url = ((HttpServletRequest) servletRequest).getRequestURL().toString();
            for (PreRegistrationController preRegistrationController : preRegistrationControllers) {
                if(!url.contains(preRegistrationController.getUrl())) { // The request requires the user to already be registered
                    String messageInitiaterId = servletRequest.getParameter(DataKeys.MESSAGE_INITIATER_ID.toString());
                    boolean isRegistered = usersDataAccess.isRegistered(messageInitiaterId);
                    if (!isRegistered) {
                        verificationOK = false;
                        logger.warning("User " + messageInitiaterId + " attempted a request in url:" + url + "but is unregistered. Request was blocked.");
                    }
                }
            }
            if(verificationOK)
                filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
