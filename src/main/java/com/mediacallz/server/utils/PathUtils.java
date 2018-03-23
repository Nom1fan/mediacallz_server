package com.mediacallz.server.utils;

import com.mediacallz.server.enums.SpecialMediaType;

/**
 * Created by Mor on 03/06/2017.
 */
public interface PathUtils {

    String getWorkingDir();

    String getUploadUrl();

    String getFolderPath(SpecialMediaType specialMediaType);

}
