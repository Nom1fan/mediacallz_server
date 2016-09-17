package com.mediacallz.server.handlers.upload_controller;

import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.SpecialMediaType;

import java.util.Map;


/**
 * Created by Mor on 25/07/2016.
 */
public interface SpMediaPathHandler {
    StringBuilder appendPathForMedia(Map<DataKeys,Object> data, StringBuilder filePathBuilder);
    SpecialMediaType getHandledSpMediaType();
}
