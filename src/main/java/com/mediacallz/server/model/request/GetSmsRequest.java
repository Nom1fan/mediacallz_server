package com.mediacallz.server.model.request;

import com.mediacallz.server.validators.InternationalUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class GetSmsRequest extends Request {

    @InternationalUID
    private String internationalPhoneNumber;
}


