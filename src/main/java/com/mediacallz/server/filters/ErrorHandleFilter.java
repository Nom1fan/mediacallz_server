package com.mediacallz.server.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mor on 23/08/2016.
 */
@Component
@Slf4j
public class ErrorHandleFilter implements Filter {

    @Override
    public void destroy() {
        // ...
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // ...
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain chain) {

        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed while handling request", ex);
            ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
