package org.example.tms.validator.custom;

import org.example.tms.dto.requests.RegisterRequestDto;
import org.example.tms.repository.UserRepository;
import org.example.tms.validator.constraint.UniqueEmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniqueEmailValidatorTest {

    @Mock
    private UserRepository userRepository;

    private UniqueEmailValidator validator;
    private RegisterRequestDto validRequest;
    private RegisterRequestDto invalidRequest;

    @BeforeEach
    public void setUp() {
        validator = new UniqueEmailValidator(userRepository);

        validRequest = RegisterRequestDto.builder()
                .email("unique@example.com")
                .build();

        invalidRequest = RegisterRequestDto.builder()
                .email("duplicate@example.com")
                .build();
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenEmailIsUnique() {
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);

        boolean isValid = validator.isValid(validRequest, null);

        assertTrue(isValid);
        verify(userRepository).existsByEmail(validRequest.getEmail());
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenEmailIsDuplicate() {
        when(userRepository.existsByEmail(invalidRequest.getEmail())).thenReturn(true);

        boolean isValid = validator.isValid(invalidRequest, null);

        assertFalse(isValid);
        verify(userRepository).existsByEmail(invalidRequest.getEmail());
    }
}
