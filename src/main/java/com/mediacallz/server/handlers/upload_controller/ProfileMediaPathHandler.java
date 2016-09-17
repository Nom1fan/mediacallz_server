package com.mediacallz.server.handlers.upload_controller;

import com.mediacallz.server.lang.ServerConstants;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.SpecialMediaType;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class ProfileMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(Map<DataKeys,Object> data, StringBuilder filePathBuilder) {
        String destId = data.get(DataKeys.DESTINATION_ID).toString();
        String extension = data.get(DataKeys.EXTENSION).toString();
        String srcWithExtension = destId + "." + extension;

        filePathBuilder.append(ServerConstants.UPLOAD_FOLDER).append(destId).append("\\").
                append(ServerConstants.PROFILE_MEDIA_RECEIVED_FOLDER).append(srcWithExtension);
        return filePathBuilder;
    }

    @Override
    public SpecialMediaType getHandledSpMediaType() {
        return SpecialMediaType.PROFILE_MEDIA;
    }
}
