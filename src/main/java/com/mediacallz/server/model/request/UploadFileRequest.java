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
public class UploadFileRequest extends Request {

    private String sourceId;
    private String sourceLocale;
    private String destinationId;
    private String destinationContactName;
    private MediaFile mediaFile;
    private String filePathOnSrcSd;
    private SpecialMediaType specialMediaType;
    private String sourceWithExtension;
}