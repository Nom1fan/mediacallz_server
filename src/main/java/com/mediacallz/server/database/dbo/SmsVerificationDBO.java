package com.mediacallz.server.database.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
