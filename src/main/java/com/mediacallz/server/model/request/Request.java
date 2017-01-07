package com.mediacallz.server.model.request;

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
public class Request {

    @NotBlank
    private String messageInitiaterId;

    @NotBlank
    private String pushToken;

    private String deviceModel;
    private String androidVersion;
    private String iosVersion;
    private String appVersion;
    private String sourceLocale;
}
