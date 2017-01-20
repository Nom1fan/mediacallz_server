package com.mediacallz.server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Mor on 1/6/2017.
 */
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, })
@Retention(RUNTIME)
@Constraint(validatedBy = MediaCallDTOValidator.class)
@Documented
public @interface HasMedia {
    String message() default "MediaCallDTO must contain at least one type of media";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        NotNull[] value();
    }
}
