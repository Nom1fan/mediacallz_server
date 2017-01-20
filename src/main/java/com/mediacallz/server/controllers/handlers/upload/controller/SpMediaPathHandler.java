package com.mediacallz.server.controllers.handlers.upload.controller;

import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.model.request.UploadFileRequest;


/**
 * Created by Mor on 25/07/2016.
 */
public interface SpMediaPathHandler {
    StringBuilder appendPathForMedia(UploadFileRequest request, StringBuilder filePathBuilder);
    SpecialMediaType getHandledSpMediaType();
}
