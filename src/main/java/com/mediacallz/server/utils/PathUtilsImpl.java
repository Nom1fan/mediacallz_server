package com.mediacallz.server.utils;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.lang.ServerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 03/06/2017.
 */
@Component
public class PathUtilsImpl implements PathUtils {

    @Value("${server.url}")
    private String serverUrl;

    private Map<SpecialMediaType, String> specialMediaType2FolderMap;

    private final String UPLOAD_FOLDER = getWorkingDir() + ServerConstants.UPLOAD_FOLDER;

    @PostConstruct
    public void init() {
        specialMediaType2FolderMap = new HashMap<SpecialMediaType, String>() {{
            put(SpecialMediaType.CALLER_MEDIA, UPLOAD_FOLDER + ServerConstants.CALLER_MEDIA_FOLDER);
            put(SpecialMediaType.PROFILE_MEDIA, UPLOAD_FOLDER + ServerConstants.PROFILE_MEDIA_FOLDER);
            put(SpecialMediaType.DEFAULT_CALLER_MEDIA, UPLOAD_FOLDER + ServerConstants.DEFAULT_CALLER_MEDIA_FOLDER);
            put(SpecialMediaType.DEFAULT_PROFILE_MEDIA, UPLOAD_FOLDER + ServerConstants.DEFAULT_PROFILE_MEDIA_FOLDER);
        }};
    }

    @Override
    public String getWorkingDir() {
        StringBuilder filePathBuilder = new StringBuilder();
        Path currentRelativePath = Paths.get("");
        // Working directory
        filePathBuilder.append(currentRelativePath.toAbsolutePath().toString());
        return filePathBuilder.toString();
    }

    @Override
    public String getUploadUrl() {
        return serverUrl + ServerConstants.UPLOAD_FOLDER;
    }



    @Override
    public String getFolderPath(SpecialMediaType specialMediaType) {
        return specialMediaType2FolderMap.get(specialMediaType);
    }
}
