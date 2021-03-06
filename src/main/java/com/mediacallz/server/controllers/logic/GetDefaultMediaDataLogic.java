package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.db.dbo.MediaTransferDBO;
import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.dto.DefaultMediaDataContainerDTO;
import com.mediacallz.server.model.dto.DefaultMediaDataDTO;
import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.model.request.GetDefaultMediaDataRequest;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.utils.FileExplorer;
import com.mediacallz.server.utils.MediaFileUtils;
import com.mediacallz.server.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mor on 1/18/2017.
 */
@Component
@Slf4j
public class GetDefaultMediaDataLogic extends AbstractServerLogic {

    @Autowired
    PathUtils pathUtils;

    @Autowired
    Dao dao;

    @Autowired
    MediaFileUtils mediaFileUtils;

    @Autowired
    FileExplorer fileExplorer;

    public Response<List<DefaultMediaDataContainerDTO>> execute(GetDefaultMediaDataRequest request) throws IOException {

        List<DefaultMediaDataContainerDTO> defaultMediaDataContainerDTOS = new ArrayList<>();

        SpecialMediaType specialMediaType = request.getSpecialMediaType();

        List<String> contactUids = request.getContactUids();

        for (String contactUid : contactUids) {
            List<DefaultMediaDataDTO> defaultMediaDataList = new ArrayList<>();

            String defaultMediaFolderPath = pathUtils.getFolderPath(specialMediaType);
            defaultMediaFolderPath += contactUid + "/";
            File[] files = fileExplorer.getFiles(defaultMediaFolderPath);
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (mediaFileUtils.isValidMediaFile(file)) {
                        MediaFileDTO mediaFileDTO = prepareMediaFile(file);
                        DefaultMediaDataDTO defaultMediaData = prepareDefaultMediaData(file, mediaFileDTO);
                        defaultMediaDataList.add(defaultMediaData);
                    }
                }
            }
            if (!defaultMediaDataList.isEmpty()) {
                DefaultMediaDataContainerDTO defaultMediaDataContainer = new DefaultMediaDataContainerDTO();
                defaultMediaDataContainer.setUid(contactUid);
                defaultMediaDataContainer.setDefaultMediaDataList(defaultMediaDataList);
                defaultMediaDataContainer.setSpecialMediaType(specialMediaType);
                defaultMediaDataContainerDTOS.add(defaultMediaDataContainer);
            }
        }

        return new Response<>(defaultMediaDataContainerDTOS);
    }

    private long getCreationDateInUnixTime(File file) throws IOException {
        Path pathToFile = Paths.get(file.getAbsolutePath());
        BasicFileAttributes attr = Files.readAttributes(pathToFile, BasicFileAttributes.class);
        return attr.creationTime().toMillis();
    }

    private DefaultMediaDataDTO prepareDefaultMediaData(File file, MediaFileDTO mediaFileDTO) throws IOException {
        DefaultMediaDataDTO defaultMediaData = new DefaultMediaDataDTO();
        long unixTime = getCreationDateInUnixTime(file);
        defaultMediaData.setDefaultMediaUnixTime(unixTime);
        defaultMediaData.setFilePathOnServer(file.getAbsolutePath());
        defaultMediaData.setMediaFile(mediaFileDTO);
        return defaultMediaData;
    }

    private MediaFileDTO prepareMediaFile(File file) {
        MediaFileDTO mediaFileDTO = new MediaFileDTO();
        mediaFileDTO.setExtension(FilenameUtils.getExtension(file.getName()));
        mediaFileDTO.setFileType(mediaFileUtils.getFileType(file));
        mediaFileDTO.setSize(file.length());
        return mediaFileDTO;
    }
}
