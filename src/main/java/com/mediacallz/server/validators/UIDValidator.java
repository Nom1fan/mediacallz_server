package com.mediacallz.server.validators;

import com.mediacallz.server.validators.handlers.uid.UIDValidationHandler;
import com.mediacallz.server.validators.handlers.uid.UIDValidationHandlersFactory;
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

    @Autowired
    private UIDValidationHandlersFactory uidValidationHandlersFactory;

    @Override
    public void initialize(Uid uid) {

    }

    @Override
    public boolean isValid(String uid, ConstraintValidatorContext context) {

        UIDValidationHandler handler = uidValidationHandlersFactory.getUidValidationHandler(serverLocale);
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
