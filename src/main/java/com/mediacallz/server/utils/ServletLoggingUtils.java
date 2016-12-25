package com.mediacallz.server.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mor on 23/08/2016.
 */
public interface ServletLoggingUtils {
    void printHeadersAndParams(HttpServletRequest request);

    String buildHeadersLog(HttpServletRequest request);

    String buildParamsLog(ServletRequest request);
}
