package org.example.tms.annotation.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.tms.validator.custom.AuthorExistsValidator;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,
        ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthorExistsValidator.class)
@Documented
public @interface UserExists {
    String message() default "User with the given ID does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
