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
@ToString
public class NotifyMediaClearedRequest extends Request {

    @Uid
    private String sourceId;


    private String destinationContactName;

    @NotNull
    private SpecialMediaType specialMediaType;
}
