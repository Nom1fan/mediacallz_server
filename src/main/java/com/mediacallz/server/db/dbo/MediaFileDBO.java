package com.mediacallz.server.db.dbo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
@RequiredArgsConstructor
public class MediaFileDBO extends DBOEntity {

    final String md5;
    final String content_ext;
    final long content_size;
    int transfer_count = 0;
    int call_count;
}
