package com.mediacallz.server.model.request;

import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class Request {

    @Uid
    private String messageInitiaterId;

    @NotBlank
    private String pushToken;

    private String deviceModel;
    private String androidVersion;
    private String iosVersion;
    private String appVersion;
    private String sourceLocale;
}
