package com.mediacallz.server.database.dbo;

import com.mediacallz.server.model.UserStatus;
import com.mediacallz.server.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.glasnost.orika.MapperFacade;

import java.util.Date;

/**
 * Created by Mor on 01/04/2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDBO extends DBOEntity {

    private String uid;
    private String token;
    private Date registered_date;
    private UserStatus userStatus;
    private Date unregistered_date;
    private int unregistered_count;
    private String deviceModel;
    private String androidVersion;
    private String iOSVersion;
    private String appVersion;
}