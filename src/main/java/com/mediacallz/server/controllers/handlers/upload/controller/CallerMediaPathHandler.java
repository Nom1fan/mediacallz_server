package com.mediacallz.server.controllers.handlers.upload.controller;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.request.UploadFileRequest;
import org.springframework.stereotype.Component;


/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class CallerMediaPathHandler extends BaseMediaPathHandler {

    private static SpecialMediaType specialMediaType = SpecialMediaType.CALLER_MEDIA;

    public CallerMediaPathHandler() {
        super(specialMediaType);
    }

    @Override
    public String appendPathForMedia(UploadFileRequest request) {
        String destId = request.getDestinationId();
        String extension = request.getMediaFile().getExtension();
        String srcWithExtension = request.getUser().getUid() + "." + extension;

        // Caller Media is saved in the destination's caller media folder,
        return getFolder() + destId + "/" + srcWithExtension;
    }

    @Override
    public SpecialMediaType getHandledSpMediaType() {
        return specialMediaType;
    }
}
