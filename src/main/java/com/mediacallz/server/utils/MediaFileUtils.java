package com.mediacallz.server.utils;

import com.mediacallz.server.model.dto.MediaFileDTO;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mor on 03/06/2017.
 */
public interface MediaFileUtils {

    String[] imageFormats = {"jpg", "png", "jpeg", "bmp", "gif", "webp"};

    List<String> imageFormatsList = Arrays.asList(imageFormats);

    String[] audioFormats = {"mp3", "ogg", "flac", "mid", "xmf", "mxmf", "rtx", "ota", "imy", "wav", "m4a", "aac"};

    List<String> audioFormatsList = Arrays.asList(audioFormats);

    String[] videoFormats = {"avi", "mpeg", "mp4", "3gp", "wmv", "webm", "mkv"};

    List<String> videoFormatsList = Arrays.asList(videoFormats);

    boolean isValidImageFormat(String pathOrUrl);

    boolean isValidAudioFormat(String pathOrUrl);

    boolean isValidVideoFormat(String pathOrUrl);

    String getFileNameByUrl(String url);

    String getFileNameWithoutExtensionByUrl(String url);

    String getFileSizeFormat(double _fileSize);

    String getMD5(String filepath);

    MediaFileDTO.FileType getFileType(String extension);

    MediaFileDTO.FileType getFileType(File file);

    MediaFileDTO.FileType getFileTypeByExtension(String extension);

    String extractExtension(String filePath);

    void deleteFilesIfNecessary(String folder, MediaFileDTO newMediaFile);

    boolean isValidMediaFile(File file);
}
