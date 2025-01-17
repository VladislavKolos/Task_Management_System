package org.example.tms.annotation.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.tms.validator.custom.TaskExistsValidator;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,
        ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TaskExistsValidator.class)
@Documented
public @interface TaskExists {
    String message() default "Task with the given ID does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
