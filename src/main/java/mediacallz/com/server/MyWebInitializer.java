package mediacallz.com.server;

/**
 * Created by Mor on 23/08/2016.
 */

import mediacallz.com.server.filters.ErrorHandleFilter;
import mediacallz.com.server.filters.RequestLogFilter;
import mediacallz.com.server.filters.VerifyUserRegisteredFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;


public class MyWebInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new ErrorHandleFilter(), new RequestLogFilter(), new VerifyUserRegisteredFilter()};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[0];
    }
}