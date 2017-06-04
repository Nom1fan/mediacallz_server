package com.mediacallz.server.validators.handlers.uid;

/**
 * Created by Mor on 03/06/2017.
 */
public interface UIDValidationHandlersFactory {

    UIDValidationHandler getUidValidationHandler(String locale);
}
