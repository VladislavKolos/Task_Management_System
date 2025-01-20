package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.tms.annotation.custom.ValidTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validator for checking if a timestamp is valid.
 * This class validates whether the given {@link LocalDateTime} is not after the current time.
 * Implements the {@link ConstraintValidator} interface to validate the timestamp.
 */
@Component
public class TimestampValidator implements ConstraintValidator<ValidTimestamp, LocalDateTime> {

    /**
     * Validates if the provided {@link LocalDateTime} is not in the future.
     * If the timestamp is {@code null}, it is considered valid.
     * Returns {@code true} if the timestamp is valid, {@code false} if the timestamp is after the current time.
     *
     * @param localDateTime              the timestamp to validate
     * @param constraintValidatorContext the context in which the constraint is being evaluated
     * @return {@code true} if the timestamp is not in the future or {@code null}, {@code false} if it is in the future
     */
    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }
        return !localDateTime.isAfter(LocalDateTime.now());
    }
}
