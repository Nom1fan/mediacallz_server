package com.mediacallz.server.validators;

import com.mediacallz.server.enums.SpecialMediaType;

import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, })
@Retention(RUNTIME)
@Constraint(validatedBy = SpecialMediaTypeValidator.class)
@Documented
public @interface SpecialMediaTypeFilter {
    SpecialMediaType[] allowOnly();

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        NotNull[] value();
    }
}
