package com.mediacallz.server.model.request;

import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class SendPushRequest extends Request {

    @Uid
    private String destinationId;

    private String title;

    private String message;

}


