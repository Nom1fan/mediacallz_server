package com.mediacallz.server.validators;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * Created by Mor on 03/06/2017.
 */
public class OptionalUIDValidator implements ConstraintValidator<OptionalUid, String> {

    @Autowired
    private UIDValidator uidValidator;

    @Override
    public void initialize(OptionalUid optionalUid) {

    }

    @Override
    public boolean isValid(String uid, ConstraintValidatorContext context) {
//        boolean isValid;
//        isValid = uid == null || uidValidator.isValid(uid, context);
//        return isValid;
        return true;
    }
}
