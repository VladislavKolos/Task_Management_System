package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.annotation.custom.ValidAuthRequest;
import org.example.tms.dto.requests.AuthenticationRequestDto;
import org.example.tms.model.User;
import org.example.tms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRequestValidator implements ConstraintValidator<ValidAuthRequest, AuthenticationRequestDto> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
