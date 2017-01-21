package com.mediacallz.server.model.request;

import com.mediacallz.server.validators.HasMedia;
import com.mediacallz.server.validators.MediaCallDTOValidator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.mediacallz.server.model.dto.MediaCallDTO;

import javax.validation.Constraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class InsertMediaCallRecordRequest extends Request {

    @Valid
    @NotNull
    @HasMedia
    private MediaCallDTO mediaCall;
}


