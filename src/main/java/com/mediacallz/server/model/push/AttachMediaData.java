package com.mediacallz.server.model.push;

import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;

/**
 * Created by Mor on 12/31/2016.
 */
@Data
public class AttachMediaData extends AbstractPushData {

    private String attachMediaGuid;
    private String sourceId;
    private String destinationId;
    private SpecialMediaType specialMediaType;
    private String mediaUrl;
}
