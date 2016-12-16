package mediacallz.com.server.filters;

import mediacallz.com.server.utils.ServletLoggingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Mor on 23/08/2016.
 */
@Component
public class RequestLogFilter implements Filter {

    @Autowired
    Logger logger;

    @Autowired
    ServletLoggingUtils servletLoggingUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            logger.config(servletLoggingUtils.buildHeadersLog((HttpServletRequest)servletRequest));
            logger.info(servletLoggingUtils.buildParamsLog(servletRequest));
        }
        else {
            logger.info(servletLoggingUtils.buildParamsLog(servletRequest));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
