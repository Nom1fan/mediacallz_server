package mediacallz.com.server.handlers.upload_controller;

import mediacallz.com.server.lang.ServerConstants;
import mediacallz.com.server.model.SpecialMediaType;
import mediacallz.com.server.model.request.UploadFileRequest;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 25/07/2016.
 */
@Component
public class DefaultProfileMediaPathHandler implements SpMediaPathHandler {

    @Override
    public StringBuilder appendPathForMedia(UploadFileRequest request, StringBuilder filePathBuilder) {
        String messageInitiaterId = request.getMessageInitiaterId();
        String extension = request.getMediaFile().getExtension();

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
