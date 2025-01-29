package org.example.tms.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.dto.requests.AuthenticationRequestDto;
import org.example.tms.model.User;
import org.example.tms.repository.UserRepository;
import org.example.tms.validator.constraint.annotation.ValidAuthRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Validator for checking the validity of an authentication request.
 * This class validates if the provided email exists in the system and if the password matches the stored password.
 * Implements the {@link ConstraintValidator} interface to validate the authentication request.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRequestValidator implements ConstraintValidator<ValidAuthRequest, AuthenticationRequestDto> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Validates the authentication request by checking if the User exists and if the provided password is correct.
     * Logs an error message if the username or password is invalid.
     *
     * @param request the authentication request containing the email and password
     * @param context the context in which the constraint is being evaluated
     * @return {@code true} if the email and password are valid, {@code false} otherwise
     */
    @Override
    public boolean isValid(AuthenticationRequestDto request, ConstraintValidatorContext context) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            log.info("Invalid Username: {}", request.getEmail());
            return false;
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.info("Invalid Password for User: {}", request.getEmail());
            return false;
        }

        return true;
    }
}
