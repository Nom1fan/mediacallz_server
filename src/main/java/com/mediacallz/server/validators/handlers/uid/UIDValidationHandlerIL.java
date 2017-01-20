package com.mediacallz.server.validators.handlers.uid;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by Mor on 1/19/2017.
 */
@Component
public class UIDValidationHandlerIL implements UIDValidationHandler {

    private String validationFailedMessage;

    @Override
    public boolean isValid(String uid) {
        boolean isValid = true;
        boolean isNumeric = org.apache.commons.lang.StringUtils.isNumeric(uid);
        if(!isNumeric) {
            validationFailedMessage = uid + " is not a numerical only value";
            isValid = false;
        }
        else if(uid.length() != 10) {
            validationFailedMessage = uid + " is not 10 digits long";
            isValid = false;
        }
        return isValid;
    }

    @Override
    public String getLocale() {
        return "IL";
    }

    @Override
    public String getValidationFailedMessage() {
        return validationFailedMessage;
    }
}
