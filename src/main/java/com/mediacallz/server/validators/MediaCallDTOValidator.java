package com.mediacallz.server.validators;

import com.mediacallz.server.model.dto.MediaCallDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Mor on 1/6/2017.
 */
public class MediaCallDTOValidator implements ConstraintValidator<HasMedia, MediaCallDTO> {

    @Override
    public void initialize(HasMedia hasMedia) {

    }

    @Override
    public boolean isValid(MediaCallDTO mediaCallDTO, ConstraintValidatorContext constraintValidatorContext) {
        return mediaCallDTO.getVisualMediaFile() != null || mediaCallDTO.getAudioMediaFile() != null;
    }
}
