package mediacallz.com.server.handlers.upload_controller;

import mediacallz.com.server.lang.ServerConstants;
import mediacallz.com.server.model.SpecialMediaType;
import mediacallz.com.server.model.request.UploadFileRequest;
import org.springframework.stereotype.Component;


/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class ProfileMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(UploadFileRequest request, StringBuilder filePathBuilder) {
        String destId = request.getDestinationId();
        String extension = request.getMediaFile().getExtension();
        String srcWithExtension = destId + "." + extension;

        filePathBuilder.append(ServerConstants.UPLOAD_FOLDER).append(destId).append("/").
                append(ServerConstants.PROFILE_MEDIA_RECEIVED_FOLDER).append(srcWithExtension);
        return filePathBuilder;
    }

    @Override
    public SpecialMediaType getHandledSpMediaType() {
        return SpecialMediaType.PROFILE_MEDIA;
    }
}
