package mediacallz.com.server.utils;

import mediacallz.com.server.model.DataKeys;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Mor on 13/09/2016.
 */
public interface RequestUtils {
    Map<DataKeys, Object> extractParametersMap(HttpServletRequest httpServletRequest);

    String getClientIpAddr(HttpServletRequest request);
}
