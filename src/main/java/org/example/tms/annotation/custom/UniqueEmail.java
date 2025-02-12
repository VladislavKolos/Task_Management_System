package org.example.tms.annotation.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.tms.validator.custom.UniqueEmailValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE,
        ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "User with this email already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
