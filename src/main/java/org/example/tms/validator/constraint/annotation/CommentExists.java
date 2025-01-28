package org.example.tms.validator.constraint.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.tms.validator.constraint.CommentExistsValidator;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,
        ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommentExistsValidator.class)
@Documented
public @interface CommentExists {
    String message() default "Comment with the given ID does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
