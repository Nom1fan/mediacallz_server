package com.mediacallz.server.model;

import com.mediacallz.server.utils.MediaFilesUtils;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.Serializable;

@Data
public class MediaFile implements Serializable {

    private String md5;
    private File _file;
    private String _extension;
    private long _size;
    private FileType _fileType;
    private String _uncompdFileFullPath;
    private boolean isCompressed = false;

    public enum FileType { IMAGE, VIDEO, AUDIO }

    public MediaFile(File file) {
        md5 = MediaFilesUtils.getMD5(file.getAbsolutePath());
        _file = file;
        _extension = FilenameUtils.getExtension(file.getAbsolutePath());
        _size = file.length();
        _fileType = MediaFilesUtils.getFileType(_extension);
    }

}
