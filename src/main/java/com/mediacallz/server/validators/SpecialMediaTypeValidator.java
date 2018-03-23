package com.mediacallz.server.validators;

import com.mediacallz.server.enums.SpecialMediaType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class SpecialMediaTypeValidator implements ConstraintValidator<SpecialMediaTypeFilter, SpecialMediaType> {

   private SpecialMediaType[] allowOnly;

   public void initialize(SpecialMediaTypeFilter constraint) {
      allowOnly = constraint.allowOnly();
   }

   public boolean isValid(SpecialMediaType specialMediaType, ConstraintValidatorContext context) {

      List<SpecialMediaType> allowOnlyList = Arrays.asList(allowOnly);
      return allowOnlyList.contains(specialMediaType);
   }
}
