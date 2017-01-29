package com.mediacallz.server.validators;

import com.mediacallz.server.validators.handlers.uid.UIDValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 1/6/2017.
 */
@Component
public class UIDValidator implements ConstraintValidator<Uid, String> {

    @Value("${server.locale}")
    private String serverLocale;

    private Map<String, UIDValidationHandler> locale2HandlerMap = new HashMap<>();;

    @Autowired
    public void initHandlers(List<UIDValidationHandler> handlers) {
        for (UIDValidationHandler handler : handlers) {
            locale2HandlerMap.put(handler.getLocale(), handler);
        }
    }

    @Override
    public void initialize(Uid uid) {

    }

    @Override
    public boolean isValid(String uid, ConstraintValidatorContext context) {
        UIDValidationHandler handler = locale2HandlerMap.get(serverLocale);
        boolean isValid = handler.isValid(uid);

        if(isValid) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(handler.getValidationFailedMessage())
                .addConstraintViolation();
        return false;
    }
}
