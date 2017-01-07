package com.mediacallz.server.filters;

import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import com.mediacallz.server.controllers.PreRegistrationController;
import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.request.Request;
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
    private Gson gson;

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
            String url = ((HttpServletRequest) servletRequest).getRequestURI();
             try {
                boolean verificationOK = true;
                ServletRequestWrapper requestWrapper = new ServletRequestWrapper((HttpServletRequest) servletRequest);
                String jsonPayload = requestWrapper.getJsonPayload();
                Request request = gson.fromJson(jsonPayload, Request.class);

                if(request == null) {
                    throw new MalformedJsonException("Request cannot be null");
                }

                String messageInitiaterId = request.getMessageInitiaterId();
                String token = request.getPushToken();

                // Verify user credentials
                if (messageInitiaterId == null || token == null) {
                    verificationOK = false;
                    sendForbiddenError((HttpServletResponse) servletResponse, url, (HttpServletRequest)servletRequest, request);
                } else if (!isPreRegistrationRequest(url)) { // The request requires the user to already be registered
                    boolean isRegistered = usersDataAccess.isRegistered(messageInitiaterId);
                    if (!isRegistered) {
                        verificationOK = false;
                        sendForbiddenError((HttpServletResponse) servletResponse, url, (HttpServletRequest)servletRequest, request);
                    }
                    UserDBO userRecord = usersDataAccess.getUserRecord(messageInitiaterId);
                    if(userRecord == null) {
                        verificationOK = false;
                        sendForbiddenError((HttpServletResponse) servletResponse, url, (HttpServletRequest)servletRequest, request);
                    }
                    else {
                        String expectedToken = userRecord.getToken();
                        if (expectedToken == null || !expectedToken.equals(token)) {
                            verificationOK = false;
                            sendForbiddenError((HttpServletResponse) servletResponse, url, (HttpServletRequest) servletRequest, request);
                        }
                    }
                }

                if (verificationOK)
                    filterChain.doFilter(requestWrapper, servletResponse);
            } catch(Exception malformedJsonException) {
                malformedJsonException.printStackTrace();
                logger.warning("Failed to process request for url:" + url +". Responding with bad request (400). [Exception]:" + malformedJsonException.getMessage());
                ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void sendForbiddenError(HttpServletResponse servletResponse, String url, HttpServletRequest servletRequest, Request request) throws IOException {
        String userId = getUserId(servletRequest, request);
        userId = (userId !=null ? userId : "anonymous");
        logger.warning("User " + userId + " attempted a request in url:" + url + " but is unregistered. Request was blocked.");
        servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    private String getUserId(HttpServletRequest servletRequest, Request request) {
        String user = null;
        if(request.getMessageInitiaterId()!=null)
            user = request.getMessageInitiaterId();
        else if(servletRequest.getRemoteUser()!=null)
            user = servletRequest.getRemoteUser();
        else if(servletRequest.getRemoteAddr()!=null)
            user = servletRequest.getRemoteAddr();

        return user;
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
