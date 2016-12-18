package mediacallz.com.server.handlers.upload_controller;

import mediacallz.com.server.model.SpecialMediaType;
import mediacallz.com.server.model.request.UploadFileRequest;


/**
 * Created by Mor on 25/07/2016.
 */
public interface SpMediaPathHandler {
    StringBuilder appendPathForMedia(UploadFileRequest request, StringBuilder filePathBuilder);
    SpecialMediaType getHandledSpMediaType();
}
