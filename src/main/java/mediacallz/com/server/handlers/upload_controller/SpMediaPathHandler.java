package mediacallz.com.server.handlers.upload_controller;

import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.SpecialMediaType;

import java.util.Map;


/**
 * Created by Mor on 25/07/2016.
 */
public interface SpMediaPathHandler {
    StringBuilder appendPathForMedia(Map<DataKeys,Object> data, StringBuilder filePathBuilder);
    SpecialMediaType getHandledSpMediaType();
}
