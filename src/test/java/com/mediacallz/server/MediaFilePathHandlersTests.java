package com.mediacallz.server;

import com.mediacallz.server.controllers.handlers.upload.controller.MediaPathHandler;
import com.mediacallz.server.controllers.handlers.upload.controller.MediaPathHandlersFactory;
import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.UploadFileRequest;
import com.mediacallz.server.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mediacallz.server.lang.ServerConstants.*;

/**
 * Created by Mor on 03/06/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MediaFilePathHandlersTests {

    @Autowired
    private MediaPathHandlersFactory mediaPathHandlersFactory;

    @Autowired
    private PathUtils pathUtils;

    @Test
    public void pathForCallerMediaTest() {

        String sourceId = "0542258808";
        String destId = "0544556543";
        String extension = "jpg";
        SpecialMediaType specialMediaType = SpecialMediaType.CALLER_MEDIA;

        UploadFileRequest request = new UploadFileRequest();
        MediaFileDTO mediaFileDTO = new MediaFileDTO();
        mediaFileDTO.setExtension(extension);
        UserDTO user = new UserDTO();
        user.setUid(sourceId);
        request.setUser(user);
        request.setDestinationId(destId);
        request.setSpecialMediaType(specialMediaType);
        request.setMediaFile(mediaFileDTO);

        MediaPathHandler pathHandler = mediaPathHandlersFactory.getPathHandler(specialMediaType);
        String actualPath = pathHandler.appendPathForMedia(request);

        String expectedPath =  pathUtils.getWorkingDir() + UPLOAD_FOLDER + CALLER_MEDIA_FOLDER + destId + "/" + sourceId + "." + extension;
        log.info("Expected:" + expectedPath);
        log.info("Actual:" + actualPath);

        Assert.assertEquals(expectedPath, actualPath);
    }

    @Test
    public void pathForProfileMediaTest() {

        String sourceId = "0542258808";
        String destId = "0544556543";
        String extension = "jpg";
        SpecialMediaType specialMediaType = SpecialMediaType.PROFILE_MEDIA;

        UploadFileRequest request = new UploadFileRequest();
        MediaFileDTO mediaFileDTO = new MediaFileDTO();
        mediaFileDTO.setExtension(extension);
        UserDTO user = new UserDTO();
        user.setUid(sourceId);
        request.setUser(user);
        request.setDestinationId(destId);
        request.setSpecialMediaType(specialMediaType);
        request.setMediaFile(mediaFileDTO);

        MediaPathHandler pathHandler = mediaPathHandlersFactory.getPathHandler(specialMediaType);
        String actualPath = pathHandler.appendPathForMedia(request);

        String expectedPath =  pathUtils.getWorkingDir() + UPLOAD_FOLDER + PROFILE_MEDIA_FOLDER + destId + "/" + sourceId + "." + extension;
        log.info("Expected:" + expectedPath);
        log.info("Actual:" + actualPath);

        Assert.assertEquals(expectedPath, actualPath);
    }

    @Test
    public void pathForDefaultCallerMediaTest() {

        String sourceId = "0542258808";
        String extension = "jpg";
        SpecialMediaType specialMediaType = SpecialMediaType.DEFAULT_CALLER_MEDIA;

        UploadFileRequest request = new UploadFileRequest();
        MediaFileDTO mediaFileDTO = new MediaFileDTO();
        mediaFileDTO.setExtension(extension);
        UserDTO user = new UserDTO();
        user.setUid(sourceId);
        request.setUser(user);
        request.setSpecialMediaType(specialMediaType);
        request.setMediaFile(mediaFileDTO);

        MediaPathHandler pathHandler = mediaPathHandlersFactory.getPathHandler(specialMediaType);
        String actualPath = pathHandler.appendPathForMedia(request);

        String expectedPath = pathUtils.getWorkingDir() + UPLOAD_FOLDER + DEFAULT_CALLER_MEDIA_FOLDER + sourceId + "/" + DEFAULT_CALLER_MEDIA_FILENAME + "." + extension;
        log.info("Expected:" + expectedPath);
        log.info("Actual:" + actualPath);

        Assert.assertEquals(expectedPath, actualPath);
    }

    @Test
    public void pathForDefaultProfileMediaTest() {

        String sourceId = "0542258808";
        String extension = "jpg";
        SpecialMediaType specialMediaType = SpecialMediaType.DEFAULT_PROFILE_MEDIA;

        UploadFileRequest request = new UploadFileRequest();
        MediaFileDTO mediaFileDTO = new MediaFileDTO();
        mediaFileDTO.setExtension(extension);
        UserDTO user = new UserDTO();
        user.setUid(sourceId);
        request.setUser(user);
        request.setSpecialMediaType(specialMediaType);
        request.setMediaFile(mediaFileDTO);

        MediaPathHandler pathHandler = mediaPathHandlersFactory.getPathHandler(specialMediaType);
        String actualPath = pathHandler.appendPathForMedia(request);

        String expectedPath = pathUtils.getWorkingDir() + UPLOAD_FOLDER + DEFAULT_PROFILE_MEDIA_FOLDER + sourceId + "/" + DEFAULT_PROFILE_MEDIA_FILENAME + "." + extension;
        log.info("Expected:" + expectedPath);
        log.info("Actual:" + actualPath);

        Assert.assertEquals(expectedPath, actualPath);
    }

}
