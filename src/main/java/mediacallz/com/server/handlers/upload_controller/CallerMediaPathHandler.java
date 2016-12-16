package mediacallz.com.server.handlers.upload_controller;

import mediacallz.com.server.lang.ServerConstants;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.SpecialMediaType;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class CallerMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(Map<DataKeys, Object> data, StringBuilder filePathBuilder) {
        String destId = data.get(DataKeys.DESTINATION_ID).toString();
        String srcWithExtension = data.get(DataKeys.SOURCE_WITH_EXTENSION).toString();

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
