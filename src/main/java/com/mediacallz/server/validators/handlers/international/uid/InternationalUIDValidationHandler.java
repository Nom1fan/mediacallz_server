package com.mediacallz.server.validators.handlers.international.uid;

/**
 * Created by Mor on 1/16/2017.
 */
public interface InternationalUIDValidationHandler {
    boolean isValid(String internationalUID);
    String getLocale();
}
