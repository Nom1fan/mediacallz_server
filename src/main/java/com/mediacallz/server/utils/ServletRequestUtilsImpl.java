package com.mediacallz.server.utils;

import com.mediacallz.server.model.DataKeys;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 13/09/2016.
 */
@Component
public class ServletRequestUtilsImpl implements ServletRequestUtils {

    @Override
    public Map<DataKeys, Object> extractParametersMap(HttpServletRequest httpServletRequest) {
        Map<DataKeys, Object> resParams = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String val = httpServletRequest.getParameter(key);
            resParams.put(DataKeys.valueOf(key), val);
        }
        return resParams;
    }
}
