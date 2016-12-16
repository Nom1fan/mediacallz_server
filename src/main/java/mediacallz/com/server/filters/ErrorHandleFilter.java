package mediacallz.com.server.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Mor on 23/08/2016.
 */
@Component
public class ErrorHandleFilter implements Filter {

    @Autowired
    private Logger logger;

    @Override
    public void destroy() {
        // ...
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // ...
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "Failed while handling request", ex);
        }

    }

}
