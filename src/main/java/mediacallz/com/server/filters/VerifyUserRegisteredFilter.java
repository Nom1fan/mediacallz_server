package mediacallz.com.server.filters;

import com.google.gson.Gson;
import mediacallz.com.server.controllers.PreRegistrationController;
import mediacallz.com.server.database.UsersDataAccess;
import mediacallz.com.server.model.request.Request;
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
            try {
                boolean verificationOK = true;
                ServletRequestWrapper requestWrapper = new ServletRequestWrapper((HttpServletRequest) servletRequest);
                String jsonPayload = requestWrapper.getJsonPayload();
                Request request = gson.fromJson(jsonPayload, Request.class);
                String messageInitiaterId = request.getMessageInitiaterId();
                String token = request.getPushToken();
                String url = requestWrapper.getRequestURI();

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
                    String expectedToken = usersDataAccess.getUserRecord(messageInitiaterId).getToken();
                    if(!expectedToken.equals(token)) {
                        verificationOK = false;
                        sendForbiddenError((HttpServletResponse) servletResponse, url, (HttpServletRequest)servletRequest, request);
                    }
                }

                if (verificationOK)
                    filterChain.doFilter(requestWrapper, servletResponse);
            } catch(Exception malformedJsonException) {
                malformedJsonException.printStackTrace();
                logger.warning("Failed to process request, responding with bad request (403). [Exception]:" + malformedJsonException.getMessage());
                ((HttpServletResponse)servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void sendForbiddenError(HttpServletResponse servletResponse, String url, HttpServletRequest servletRequest, Request request) throws IOException {
        String userId = getUserId(servletRequest, request);
        userId = (userId !=null ? userId : "anonymous");
        logger.warning("User " + userId + " attempted a request in url:" + url + " but is unregistered. Request was blocked.");
        servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
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
