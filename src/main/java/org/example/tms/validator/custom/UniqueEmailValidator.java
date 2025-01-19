package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.annotation.custom.UniqueEmail;
import org.example.tms.dto.requests.RegisterRequestDto;
import org.example.tms.repository.UserRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, RegisterRequestDto> {
    private final UserRepository userRepository;

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
