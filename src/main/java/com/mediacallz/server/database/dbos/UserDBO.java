package com.mediacallz.server.database.dbos;

import com.mediacallz.server.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Mor on 01/04/2016.
 */
@Data
@AllArgsConstructor
@ToString
public class UserDBO {

    private String uid;
    private String token;
    private Date registered_date;
    private UserStatus userStatus;
    private Date unregistered_date;
    private int unregistered_count;
    private String deviceModel;
    private String androidVersion;
}
