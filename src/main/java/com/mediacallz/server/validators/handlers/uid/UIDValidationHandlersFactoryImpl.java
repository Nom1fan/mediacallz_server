package com.mediacallz.server.validators.handlers.uid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 03/06/2017.
 */
@Component
public class UIDValidationHandlersFactoryImpl implements UIDValidationHandlersFactory {

    private Map<String, UIDValidationHandler> locale2HandlerMap = new HashMap<>();;

    @Autowired
    public void initHandlers(List<UIDValidationHandler> handlers) {
        for (UIDValidationHandler handler : handlers) {
            locale2HandlerMap.put(handler.getLocale(), handler);
        }
    }

    @Override
    public UIDValidationHandler getUidValidationHandler(String locale) {
        return locale2HandlerMap.get(locale);
    }
}
