package com.mediacallz.server.model.dto;

import com.mediacallz.server.enums.OsType;
import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import com.mediacallz.server.db.dbo.UserDBO;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Mor on 21/12/2016.
 */
@Data
public class UserDTO extends DTOEntity<UserDBO> {

    @Uid
    private String uid;

    @NotBlank
    private String token;

    private UserStatus userStatus;

    @NotBlank
    private String deviceModel;

    private OsType os;

    @NotBlank
    private String osVersion;

    @NotBlank
    private String appVersion;
}
