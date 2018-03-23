package com.mediacallz.server.validators;

import org.apache.commons.validator.UrlValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class URLValidator implements ConstraintValidator<Url, String> {
    @Override
    public void initialize(Url constraintAnnotation) {

    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }
}
