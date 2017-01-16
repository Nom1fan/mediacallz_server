package com.mediacallz.server.validators;

import com.mediacallz.server.validators.handlers.international.uid.InternationalUIDValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 1/16/2017.
 */
@Component
public class InternationalUIDValidator implements ConstraintValidator<InternationalUID, String> {

    @Value("${server.locale}")
    private String serverLocale;

    private Map<String, InternationalUIDValidationHandler> locale2HandlerMap = new HashMap<>();;

    @Autowired
    public void initHandlers(List<InternationalUIDValidationHandler> handlers) {
        for (InternationalUIDValidationHandler handler : handlers) {
            locale2HandlerMap.put(handler.getLocale(), handler);
        }
    }

    @Override
    public void initialize(InternationalUID internationalUID) {

    }

    @Override
    public boolean isValid(String internationalUID, ConstraintValidatorContext context) {
        InternationalUIDValidationHandler handler = locale2HandlerMap.get(serverLocale);
        return handler.isValid(internationalUID);
    }
}
