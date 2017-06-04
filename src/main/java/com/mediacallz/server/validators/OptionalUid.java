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
@Constraint(validatedBy = OptionalUIDValidator.class)
@Documented
public @interface OptionalUid {
    String message() default "User ID must be exactly 10 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        NotNull[] value();
    }
}
