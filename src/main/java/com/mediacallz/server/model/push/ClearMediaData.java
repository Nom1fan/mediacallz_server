package com.mediacallz.server.model.push;

import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;

/**
 * Created by Mor on 12/31/2016.
 */
@Data
public class ClearMediaData extends AbstractPushData {

    private String sourceId;
    private String sourceLocale;
    private SpecialMediaType specialMediaType;
}
