package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.custom.UserExists;
import org.example.tms.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validator for checking whether a User with the specified ID exists in the system.
 * Implements the {@link ConstraintValidator} interface to validate the existence of a User.
 */
@Component
@RequiredArgsConstructor
public class AuthorExistsValidator implements ConstraintValidator<UserExists, UUID> {
    private final UserRepository userRepository;

    /**
     * Validates whether a User exists in the system based on the provided user ID.
     * Returns {@code false} if the User ID is {@code null} or if no user with the given ID exists.
     *
     * @param userId                     the UUID of the User to be checked
     * @param constraintValidatorContext context in which the constraint is being evaluated
     * @return {@code true} if the User exists; {@code false} otherwise
     */
    @Override
    public boolean isValid(UUID userId, ConstraintValidatorContext constraintValidatorContext) {
        if (userId == null) {
            return false;
        }
        return userRepository.existsById(userId);
    }
}
