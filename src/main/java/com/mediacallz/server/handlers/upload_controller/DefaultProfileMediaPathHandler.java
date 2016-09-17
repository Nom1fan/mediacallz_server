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
public class DefaultProfileMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(Map<DataKeys,Object> data, StringBuilder filePathBuilder) {
        String messageInitiaterId = data.get(DataKeys.DESTINATION_ID).toString();
        String extension = data.get(DataKeys.EXTENSION).toString();

        filePathBuilder.append(ServerConstants.UPLOAD_FOLDER).append(messageInitiaterId).append("\\").
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
