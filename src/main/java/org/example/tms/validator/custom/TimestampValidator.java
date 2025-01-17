package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.tms.annotation.custom.ValidTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampValidator implements ConstraintValidator<ValidTimestamp, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }
        return !localDateTime.isAfter(LocalDateTime.now());
    }
}
