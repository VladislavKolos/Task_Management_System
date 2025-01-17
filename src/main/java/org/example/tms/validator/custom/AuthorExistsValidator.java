package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.custom.UserExists;
import org.example.tms.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorExistsValidator implements ConstraintValidator<UserExists, UUID> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(UUID userId, ConstraintValidatorContext constraintValidatorContext) {
        if (userId == null) {
            return false;
        }
        return userRepository.existsById(userId);
    }
}
