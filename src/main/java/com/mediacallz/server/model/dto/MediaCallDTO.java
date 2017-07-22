package com.mediacallz.server.model.dto;

import com.mediacallz.server.db.dbo.MediaCallDBO;
import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 10/03/2016.
 */
@Data
public class MediaCallDTO extends DTOEntity<MediaCallDBO> {

    @NotNull
    @NotEmpty
    private String sourceId;

    @NotNull
    @NotEmpty
    private String destinationId;

    @Valid
    private MediaFileDTO visualMediaFile;

    @Valid
    private MediaFileDTO audioMediaFile;

    @NotNull
    private SpecialMediaType specialMediaType;
}
