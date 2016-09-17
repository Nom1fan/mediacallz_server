package com.mediacallz.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Mor on 23/08/2016.
 */
@Component
public class ServletLoggingUtilsImpl implements ServletLoggingUtils {

    @Autowired
    private Logger logger;

    @Override
    public void printHeadersAndParams(HttpServletRequest request) {
        logger.info(buildHeadersLog(request));
        logger.info(buildParamsLog(request));
    }

    @Override
    public String buildHeadersLog(HttpServletRequest request) {
        StringBuilder headerBuilder = new StringBuilder("Request headers:[");
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            headerBuilder.append(headerName).append("=").append(request.getHeader(headerName))
                    .append(",");
        }
        headerBuilder.deleteCharAt(headerBuilder.lastIndexOf(","));
        headerBuilder.append("]");
        return headerBuilder.toString();
    }

    @Override
    public String buildParamsLog(ServletRequest request) {
        StringBuilder paramsBuilder = new StringBuilder("Request params:[");
        Set<String> keySet = request.getParameterMap().keySet();
        if(!keySet.isEmpty()) {
            for (String key : keySet) {
                paramsBuilder.append(key).append("=").append(request.getParameter(key))
                        .append(",");
            }
            paramsBuilder.deleteCharAt(paramsBuilder.lastIndexOf(","));
        }
        paramsBuilder.append("]");
        return paramsBuilder.toString();
    }
}
