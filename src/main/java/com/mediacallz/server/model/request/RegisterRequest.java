package com.mediacallz.server.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class RegisterRequest extends Request {

    private int smsCode;
}


