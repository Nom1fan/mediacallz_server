package com.mediacallz.server.model.request;

import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class ClearMediaRequest extends Request {

    @Uid
    private String destinationId;

    @Uid
    private String sourceId;

    @NotNull
    private SpecialMediaType specialMediaType;

    private String destinationContactName;
}
