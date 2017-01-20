package com.mediacallz.server.model.dto;

import com.mediacallz.server.model.UserStatus;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import com.mediacallz.server.database.dbo.UserDBO;

/**
 * Created by Mor on 21/12/2016.
 */
@Data
public class UserDTO extends DTOEntity<UserDBO> {

    @Uid
    private String uid;

    private UserStatus userStatus;
}
