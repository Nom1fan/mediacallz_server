package com.mediacallz.server.model.dto;

import com.mediacallz.server.database.dbo.MediaCallDBO;
import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 10/03/2016.
 */
@Data
@ToString
public class MediaCallDTO extends DTOEntity<MediaCallDBO> {

    @NotNull
    @NotEmpty
    private String sourceId;

    @NotNull
    @NotEmpty
    private String destinationId;

    @Valid
    private MediaFileDTO visualMediaFileDTO;

    @Valid
    private MediaFileDTO audioMediaFileDTO;

    @NotNull
    private SpecialMediaType specialMediaType;
}
