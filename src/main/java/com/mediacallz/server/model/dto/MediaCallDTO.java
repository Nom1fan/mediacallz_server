package com.mediacallz.server.model.dto;

import com.mediacallz.server.database.dbo.MediaCallDBO;
import com.mediacallz.server.model.MediaFile;
import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;
import lombok.ToString;
import ma.glasnost.orika.MapperFacade;

/**
 * Created by Mor on 10/03/2016.
 */
@Data
@ToString
public class MediaCallDTO extends DTOEntity<MediaCallDBO> {

    private String sourceId;
    private String destinationId;

    private MediaFile visualMediaFile;
    private MediaFile audioMediaFile;

    private SpecialMediaType specialMediaType;
}
