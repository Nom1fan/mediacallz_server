package com.mediacallz.server.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mor on 13/09/2016.
 */
public interface RequestUtils {

    String getClientIpAddr(HttpServletRequest request);
}
