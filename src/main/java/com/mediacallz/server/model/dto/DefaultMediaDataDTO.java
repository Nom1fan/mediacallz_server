package com.mediacallz.server.model.dto;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.dto.MediaFileDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Mor on 24/05/2017.
 */
@Data
public class DefaultMediaDataDTO extends DTOEntity {

    private String uid;

    private MediaFileDTO mediaFile;

    private long defaultMediaUnixTime;

    private String filePathOnServer;

    private SpecialMediaType specialMediaType;
}
