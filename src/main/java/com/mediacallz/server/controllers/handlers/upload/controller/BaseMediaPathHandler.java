package com.mediacallz.server.controllers.handlers.upload.controller;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 03/06/2017.
 */
@Component
public abstract class BaseMediaPathHandler implements MediaPathHandler {

    protected SpecialMediaType specialMediaType;

    @Autowired
    private PathUtils pathUtils;

    public BaseMediaPathHandler(SpecialMediaType specialMediaType) {
        this.specialMediaType = specialMediaType;
    }


    protected String getFolder() {
        return pathUtils.getFolderPath(specialMediaType);
    }

}
