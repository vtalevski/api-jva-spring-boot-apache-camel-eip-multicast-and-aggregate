package com.talevski.viktor.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = PARAMETER)
@Retention(value = RUNTIME)
@Constraint(validatedBy = ScientistRequestValidator.class)
public @interface ValidatedScientistRequest {
    String message() default "Default validation error message for an invalid scientist request.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
