package com.mediacallz.server.model.request;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
public class ClearMediaRequest extends Request {

    @Uid
    private String destinationId;

    @Uid
    private String sourceId;

    @NotNull
    private SpecialMediaType specialMediaType;

    private String destinationContactName;
}
