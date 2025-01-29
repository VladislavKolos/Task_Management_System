package org.example.tms.validator.constraint.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.tms.validator.constraint.AuthRequestValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthRequestValidator.class)
@Documented
public @interface ValidAuthRequest {
    String message() default "Invalid username or password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
