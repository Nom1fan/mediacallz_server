package com.mediacallz.server.model.push;

import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;

/**
 * Created by Mor on 1/3/2017.
 */
@Data
public class ClearSuccessData extends AbstractPushData {

    private String destinationId;
    private SpecialMediaType specialMediaType;

}
