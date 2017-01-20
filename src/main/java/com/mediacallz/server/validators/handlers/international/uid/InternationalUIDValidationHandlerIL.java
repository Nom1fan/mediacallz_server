package com.mediacallz.server.validators.handlers.international.uid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 1/16/2017.
 */
@Component
public class InternationalUIDValidationHandlerIL implements InternationalUIDValidationHandler {
    @Override
    public boolean isValid(String internationalUID) {
        boolean isNumeric = StringUtils.isNumeric(internationalUID);
        return internationalUID.startsWith("972") && internationalUID.length() == 12 && isNumeric;
    }

    @Override
    public String getLocale() {
        return "IL";
    }
}
