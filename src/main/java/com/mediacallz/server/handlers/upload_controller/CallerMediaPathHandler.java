package com.mediacallz.server.handlers.upload_controller;

import com.mediacallz.server.lang.ServerConstants;
import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.model.request.UploadFileRequest;
import org.springframework.stereotype.Component;


/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class CallerMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(UploadFileRequest request, StringBuilder filePathBuilder) {
        String destId = request.getDestinationId();
        String srcWithExtension = request.getSourceWithExtension();

        // Caller Media is saved in the destination's caller media folder,
        filePathBuilder.append(ServerConstants.UPLOAD_FOLDER).append(destId).append("/").
                append(ServerConstants.CALLER_MEDIA_FOLDER).append(srcWithExtension);
        return filePathBuilder;
    }

    @Override
    public SpecialMediaType getHandledSpMediaType() {
        return SpecialMediaType.CALLER_MEDIA;
    }
}
