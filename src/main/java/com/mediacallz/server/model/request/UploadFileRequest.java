package com.mediacallz.server.model.request;

import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.OptionalUid;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class UploadFileRequest extends Request {

    @Uid
    @Deprecated
    private String sourceId;

    @OptionalUid
    private String destinationId;

    private String destinationContactName;

    @NotNull
    private MediaFileDTO mediaFile;

    private String filePathOnSrcSd;

    private SpecialMediaType specialMediaType;
}
