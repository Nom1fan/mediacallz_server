package com.mediacallz.server.db.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsVerificationDBO extends DBOEntity {

    private String uid;
    private int code;

}
