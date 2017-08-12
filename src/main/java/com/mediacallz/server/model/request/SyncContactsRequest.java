package com.mediacallz.server.model.request;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
public class SyncContactsRequest extends Request {

    @Valid
    @NotEmpty
    private List<ContactDTO> contacts;
}
