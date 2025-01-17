package org.example.tms.annotation.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.tms.validator.custom.TimestampValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimestampValidator.class)
@Documented
public @interface ValidTimestamp {
    String message() default "Timestamp must not be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
