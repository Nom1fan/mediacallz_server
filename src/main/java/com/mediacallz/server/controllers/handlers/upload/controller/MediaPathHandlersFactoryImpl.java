package com.mediacallz.server.controllers.handlers.upload.controller;

import com.mediacallz.server.enums.SpecialMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 03/06/2017.
 */
@Component
public class MediaPathHandlersFactoryImpl implements MediaPathHandlersFactory {

    private final Map<SpecialMediaType, MediaPathHandler> specialMediaType2UploadPathHandlerMap = new HashMap<>();

    @Autowired
    public void initMap(List<MediaPathHandler> mediaPathHandlerList) {
        for (MediaPathHandler mediaPathHandler : mediaPathHandlerList) {
            specialMediaType2UploadPathHandlerMap.put(mediaPathHandler.getHandledSpMediaType(), mediaPathHandler);
        }
    }

    @Override
    public MediaPathHandler getPathHandler(SpecialMediaType specialMediaType) {
        return specialMediaType2UploadPathHandlerMap.get(specialMediaType);
    }
}
