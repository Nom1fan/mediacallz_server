package com.mediacallz.server.utils;

import com.mediacallz.server.model.DataKeys;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Mor on 13/09/2016.
 */
public interface ServletRequestUtils {
    Map<DataKeys, Object> extractParametersMap(HttpServletRequest httpServletRequest);
}
