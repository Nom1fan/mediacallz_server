package com.mediacallz.server.validators.handlers.uid;

/**
 * Created by Mor on 1/19/2017.
 */
public interface UIDValidationHandler {
    boolean isValid(String uid);
    String getLocale();
    String getValidationFailedMessage();

}
