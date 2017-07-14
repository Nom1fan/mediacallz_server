package com.mediacallz.server.model.dto;

import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;

/**
 * Created by Mor on 24/05/2017.
 */
@Data
public class DefaultMediaDataDTO extends DTOEntity {

    private MediaFileDTO mediaFile;

    private long defaultMediaUnixTime;

    private String filePathOnServer;
}
