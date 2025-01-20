package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.annotation.custom.UniqueEmail;
import org.example.tms.dto.requests.RegisterRequestDto;
import org.example.tms.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Validator for checking the uniqueness of an email.
 * This class validates that the provided email in the {@link RegisterRequestDto} is not already registered in the system.
 * Implements the {@link ConstraintValidator} interface to ensure the email is unique.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, RegisterRequestDto> {
    private final UserRepository userRepository;

    /**
     * Validates whether the email in the provided {@link RegisterRequestDto} is unique.
     * Checks if the email already exists in the database. If it does, returns {@code false} and logs the event.
     * If the email does not exist, returns {@code true}.
     *
     * @param registerRequestDto         the data transfer object containing the email to validate
     * @param constraintValidatorContext the context in which the constraint is being evaluated
     * @return {@code true} if the email is unique, {@code false} if it already exists
     */
    @Override
    public boolean isValid(RegisterRequestDto registerRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {

        boolean exists = userRepository.existsByEmail(registerRequestDto.getEmail());
        if (exists) {
            log.info("User with email {} is already registered", registerRequestDto.getEmail());
            return false;
        }

        return true;
    }
}
