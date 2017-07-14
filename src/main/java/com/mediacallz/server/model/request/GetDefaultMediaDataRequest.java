package com.mediacallz.server.model.request;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.DefaultMediaOnly;
import com.mediacallz.server.validators.Uid;
import com.mediacallz.server.validators.UidsList;
import lombok.Data;

import java.util.List;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
public class GetDefaultMediaDataRequest extends Request {

    @UidsList
    private List<String> contactUids;

    @DefaultMediaOnly
    private SpecialMediaType specialMediaType;

}


