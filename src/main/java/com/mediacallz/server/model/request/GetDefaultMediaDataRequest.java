package com.mediacallz.server.model.request;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.DefaultMediaOnly;
import com.mediacallz.server.validators.Uid;
import lombok.Data;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
public class GetDefaultMediaDataRequest extends Request {

    @Uid
    private String uid;

    @DefaultMediaOnly
    private SpecialMediaType specialMediaType;

}


