package com.mediacallz.server.model.request;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class DownloadFileRequest extends Request {

    //@Min(1) //TODO commId should not be here, we should completely change the flow of upload/download
    private int commId;

    @Uid
    private String sourceId;

    @Uid
    private String destinationId;

    private String destinationContactName;

    @NotBlank
    private String filePathOnServer;

    @NotNull
    private SpecialMediaType specialMediaType;

    @NotBlank
    private String filePathOnSrcSd;
}
