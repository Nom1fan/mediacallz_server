package com.mediacallz.server.validators;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.dto.MediaCallDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.mediacallz.server.enums.SpecialMediaType.*;

/**
 * Created by Mor on 1/6/2017.
 */
public class DefaultMediaOnlyValidator implements ConstraintValidator<DefaultMediaOnly, SpecialMediaType> {

    @Override
    public void initialize(DefaultMediaOnly hasMedia) {

    }

    @Override
    public boolean isValid(SpecialMediaType specialMediaType, ConstraintValidatorContext constraintValidatorContext) {
        return specialMediaType.equals(DEFAULT_CALLER_MEDIA) || specialMediaType.equals(DEFAULT_PROFILE_MEDIA);
    }
}
