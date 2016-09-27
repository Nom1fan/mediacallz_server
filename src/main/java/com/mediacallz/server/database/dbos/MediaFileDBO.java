package com.mediacallz.server.database.dbos;

import lombok.Data;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
public class MediaFileDBO {

    String md5;
    String content_ext;
    long content_size;
    String transfer_count;
    String call_count;


    public MediaFileDBO(String md5, String fileExtension, long fileSize) {
        this.md5 = md5;
        this.content_ext = fileExtension;
        this.content_size = fileSize;
    }
}
