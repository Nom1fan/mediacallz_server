package com.mediacallz.server.controllers.handlers.upload.controller;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.lang.ServerConstants;
import com.mediacallz.server.model.request.UploadFileRequest;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class DefaultCallerMediaPathHandler extends BaseMediaPathHandler {

    private static SpecialMediaType specialMediaType = SpecialMediaType.DEFAULT_CALLER_MEDIA;

    public DefaultCallerMediaPathHandler() {
        super(specialMediaType);
    }

    @Override
    public String appendPathForMedia(UploadFileRequest request) {
        return getFolder() + request.getUser().getUid() + "/" + ServerConstants.DEFAULT_CALLER_MEDIA_FILENAME + "." + request.getMediaFile().getExtension();
    }

    @Override
    public SpecialMediaType getHandledSpMediaType() {
        return specialMediaType;
    }
}
