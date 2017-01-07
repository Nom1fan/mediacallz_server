package com.mediacallz.server.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Mor on 1/6/2017.
 */
public class UIDValidator implements ConstraintValidator<Uid, String> {

    @Override
    public void initialize(Uid uid) {

    }

    @Override
    public boolean isValid(String uid, ConstraintValidatorContext constraintValidatorContext) {
        return uid != null && uid.length() == 10;
    }
}
