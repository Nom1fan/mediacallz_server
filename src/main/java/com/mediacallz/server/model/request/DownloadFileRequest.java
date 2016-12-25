package com.mediacallz.server.model.request;

import com.mediacallz.server.model.MediaFile;
import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class DownloadFileRequest extends Request {

    private int commId;
    private String sourceId;
    private String destinationId;
    private String destinationContactName;
    private String sourceLocale;
    private String filePathOnServer;
    private SpecialMediaType specialMediaType;
    private MediaFile.FileType fileType;
    private String filePathOnSrcSd;
}
