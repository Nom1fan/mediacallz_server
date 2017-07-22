package com.mediacallz.server.model.dto;

import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.validators.Uid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Mor on 22/07/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO extends DTOEntity<ContactDBO> {

    @Uid
    private String contactUid;

    @NotBlank
    private String contactName;

    @Uid
    private String contactSource;
}
