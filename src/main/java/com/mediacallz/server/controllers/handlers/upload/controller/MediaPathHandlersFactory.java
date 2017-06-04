package com.mediacallz.server.controllers.handlers.upload.controller;

import com.mediacallz.server.enums.SpecialMediaType;

/**
 * Created by Mor on 03/06/2017.
 */
public interface MediaPathHandlersFactory {

    MediaPathHandler getPathHandler(SpecialMediaType specialMediaType);
}
