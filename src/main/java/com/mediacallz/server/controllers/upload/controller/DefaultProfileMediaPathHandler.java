package com.mediacallz.server.controllers.upload.controller;

import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.lang.ServerConstants;
import com.mediacallz.server.model.request.UploadFileRequest;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class DefaultProfileMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(UploadFileRequest request, StringBuilder filePathBuilder) {
        String messageInitiaterId = request.getMessageInitiaterId();
        String extension = request.getMediaFileDTO().getExtension();

        filePathBuilder.append(ServerConstants.UPLOAD_FOLDER).append(messageInitiaterId).append("/").
                append(ServerConstants.MY_DEFAULT_PROFILE_MEDIA_FOLDER).
                append(ServerConstants.MY_DEFAULT_PROFILE_MEDIA_FILENAME).
                append(extension);
        return filePathBuilder;
    }

    @Override
    public SpecialMediaType getHandledSpMediaType() {
        return SpecialMediaType.MY_DEFAULT_PROFILE_MEDIA;
    }
}
