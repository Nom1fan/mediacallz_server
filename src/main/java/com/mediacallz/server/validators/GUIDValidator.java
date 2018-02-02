package com.mediacallz.server.validators;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mor on 1/6/2017.
 */
@Component
public class GUIDValidator implements ConstraintValidator<Guid, String> {

    @Override
    public void initialize(Guid guid) {

    }

    @Override
    public boolean isValid(String guid, ConstraintValidatorContext context) {
        Pattern guidPattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        Matcher matcher = guidPattern.matcher(guid);
        return matcher.matches();
    }
}
