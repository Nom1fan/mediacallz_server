package com.mediacallz.server.utils;

import com.mediacallz.server.model.dto.MediaFileDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mor on 01/07/2016.
 */
@Component
public class MediaFilesUtilsImpl implements MediaFileUtils {

    @Autowired
    FileExplorer fileExplorer;

    @Override
    public boolean isValidImageFormat(String pathOrUrl) {
        String extension = pathOrUrl.substring(pathOrUrl.lastIndexOf(".") + 1);
        return imageFormatsList.contains(extension.toLowerCase());
    }

    @Override
    public boolean isValidAudioFormat(String pathOrUrl) {
        String extension = pathOrUrl.substring(pathOrUrl.lastIndexOf(".") + 1);
        return audioFormatsList.contains(extension.toLowerCase());
    }

    @Override
    public boolean isValidVideoFormat(String pathOrUrl) {
        String extension = pathOrUrl.substring(pathOrUrl.lastIndexOf(".") + 1);
        return videoFormatsList.contains(extension.toLowerCase());
    }

    @Override
    public String getFileNameByUrl(String url) {
        return FilenameUtils.getName(url).replaceAll("%20", " ");
    }

    @Override
    public String getFileNameWithoutExtensionByUrl(String url) {
        return FilenameUtils.getBaseName(url).replaceAll("%20", " ");
    }

    @Override
    public String getFileSizeFormat(double _fileSize) {

        double MB = (int) Math.pow(2, 20);
        double KB = (int) Math.pow(2, 10);
        DecimalFormat df = new DecimalFormat("#.00"); // rounding to max 2 decimal places

        if (_fileSize >= MB) {
            double fileSizeInMB = _fileSize / MB; // File size in MBs
            return df.format(fileSizeInMB) + "MB";
        } else if (_fileSize >= KB) {
            double fileSizeInKB = _fileSize / KB; // File size in KBs
            return df.format(fileSizeInKB) + "KB";
        }

        // File size in Bytes
        return df.format(_fileSize) + "B";
    }

    @Override
    public String getMD5(String filepath) {

        try {

            InputStream input = new FileInputStream(filepath);
            byte[] buffer = new byte[1024];

            MessageDigest hashMsgDigest = null;
            hashMsgDigest = MessageDigest.getInstance("MD5");

            int read;
            do {
                read = input.read(buffer);
                if (read > 0) {
                    hashMsgDigest.update(buffer, 0, read);
                }
            } while (read != -1);
            input.close();

            StringBuffer hexString = new StringBuffer();
            byte[] hash = hashMsgDigest.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0"
                            + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MediaFileDTO.FileType getFileType(String extension) {

        if (Arrays.asList(imageFormats).contains(extension))
            return MediaFileDTO.FileType.IMAGE;
        else if (Arrays.asList(audioFormats).contains(extension))
            return MediaFileDTO.FileType.AUDIO;
        else if (Arrays.asList(videoFormats).contains(extension))
            return MediaFileDTO.FileType.VIDEO;

        return null;
    }

    @Override
    public MediaFileDTO.FileType getFileType(File file) {
        return getFileTypeByExtension(extractExtension(file.getAbsolutePath()));
    }

    @Override
    public MediaFileDTO.FileType getFileTypeByExtension(String extension) {
        MediaFileDTO.FileType fileType = null;
        if (Arrays.asList(imageFormats).contains(extension)) {
            fileType = MediaFileDTO.FileType.IMAGE;
        } else if (Arrays.asList(audioFormats).contains(extension)) {
            fileType = MediaFileDTO.FileType.AUDIO;
        } else if (Arrays.asList(videoFormats).contains(extension)) {
            fileType = MediaFileDTO.FileType.VIDEO;
        }
        return fileType;
    }

    @Override
    public String extractExtension(String filePath) {
        String ext = null;
        String tmp_str[] = filePath.split("\\.(?=[^\\.]+$)"); // getting last
        if (tmp_str.length >= 2) {
            ext = tmp_str[1].toLowerCase();
        }
        return ext;
    }

    /**
     * Deletes files in the source's designated directory by an algorithm based on the new media file type:
     * This method does not delete the new downloaded file.
     * lets mark newFileType as NFT.
     * NFT = IMAGE --> deletes video
     * NFT = AUDIO --> deletes video
     * NFT = VIDEO --> deletes image and audio
     *
     * @param folder       The folder to scan the files in
     * @param newMediaFile The new media file
     */
    @Override
    public void deleteFilesIfNecessary(String folder, MediaFileDTO newMediaFile) {

        File[] files = fileExplorer.getFiles(folder);

        if (files == null) {
            return;
        }

        MediaFileDTO.FileType newFileType = newMediaFile.getFileType();
        switch (newFileType) {
            case AUDIO: {
                for (File file : files) {
                    MediaFileDTO.FileType fileType = getFileType(file);
                    if (fileType.equals(MediaFileDTO.FileType.VIDEO)) {
                        fileExplorer.delete(file);
                    }
                }
            }
            break;
            case IMAGE: {
                for (File file : files) {
                    MediaFileDTO.FileType fileType = getFileType(file);
                    if (fileType == MediaFileDTO.FileType.VIDEO) {
                        fileExplorer.delete(file);
                    }
                }
            }
            break;
            case VIDEO: {
                for (File file : files) {
                    MediaFileDTO.FileType fileType = getFileType(file);
                    if (fileType.equals(MediaFileDTO.FileType.IMAGE) ||
                            fileType.equals(MediaFileDTO.FileType.AUDIO)) {
                        fileExplorer.delete(file);
                    }
                }
            }
            break;
        }
    }

    @Override
    public boolean isValidMediaFile(File file) {
        String path = file.getAbsolutePath();
        return isValidImageFormat(path) || isValidAudioFormat(path) || isValidVideoFormat(path);
    }

    public void setFileExplorer(FileExplorer fileExplorer) {
        this.fileExplorer = fileExplorer;
    }
}
