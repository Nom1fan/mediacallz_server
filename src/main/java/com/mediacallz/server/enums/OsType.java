package com.mediacallz.server.enums;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Mor on 1/29/2017.
 */
@NoArgsConstructor
public enum OsType {

    IOS("IOS"),
    ANDROID("ANDROID"),
    WEB("WEB");

    @Getter
    private String osType;

    OsType(String osType) {
        this.osType = osType;
    }

}
