package com.mediacallz.server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Mor on 03/06/2017.
 */
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, })
@Retention(RUNTIME)
@Constraint(validatedBy = DefaultMediaOnlyValidator.class)
@Documented
public @interface DefaultMediaOnly {
    String message() default "Special media type must be default type only";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        NotNull[] value();
    }
}
