package com.mediacallz.server.validators.handlers.international.uid;

import org.springframework.stereotype.Component;

/**
 * Created by Mor on 1/16/2017.
 */
@Component
public class ILInternationalUIDValidationHandler implements InternationalUIDValidationHandler {
    @Override
    public boolean isValid(String internationalUID) {
        return internationalUID.startsWith("972") && internationalUID.length() == 12;
    }

    @Override
    public String getLocale() {
        return "IL";
    }
}
