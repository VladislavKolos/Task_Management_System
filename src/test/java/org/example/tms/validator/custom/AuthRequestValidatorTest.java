package org.example.tms.validator.custom;

import org.example.tms.dto.requests.AuthenticationRequestDto;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.example.tms.repository.UserRepository;
import org.example.tms.validator.constraint.AuthRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthRequestValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthRequestValidator validator;

    private AuthenticationRequestDto validRequest;
    private AuthenticationRequestDto invalidRequest;
    private AuthenticationRequestDto invalidPasswordRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        validator = new AuthRequestValidator(userRepository, passwordEncoder);

        validRequest = AuthenticationRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .build();

        invalidRequest = AuthenticationRequestDto.builder()
                .email("invalid@example.com")
                .password("password")
                .build();

        invalidPasswordRequest = AuthenticationRequestDto.builder()
                .email("test@example.com")
                .password("wrongPassword")
                .build();

        user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.ROLE_USER)
                .build();
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenValidEmailAndPassword() {
        when(userRepository.findByEmail(validRequest.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(validRequest.getPassword(), user.getPassword()))
                .thenReturn(true);

        boolean isValid = validator.isValid(validRequest, null);

        assertTrue(isValid);
        verify(userRepository)
                .findByEmail(validRequest.getEmail());
        verify(passwordEncoder)
                .matches(validRequest.getPassword(), user.getPassword());
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenUserNotFound() {
        when(userRepository.findByEmail(invalidRequest.getEmail()))
                .thenReturn(Optional.empty());

        boolean isValid = validator.isValid(invalidRequest, null);

        assertFalse(isValid);
        verify(userRepository)
                .findByEmail(invalidRequest.getEmail());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
        when(userRepository.findByEmail(invalidPasswordRequest.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(invalidPasswordRequest.getPassword(), user.getPassword()))
                .thenReturn(false);

        boolean isValid = validator.isValid(invalidPasswordRequest, null);

        assertFalse(isValid);
        verify(userRepository)
                .findByEmail(invalidPasswordRequest.getEmail());
        verify(passwordEncoder)
                .matches(invalidPasswordRequest.getPassword(), user.getPassword());
    }
}
