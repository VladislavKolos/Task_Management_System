package org.example.tms.validator.custom;

import org.example.tms.repository.UserRepository;
import org.example.tms.validator.constraint.AuthorExistsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorExistsValidatorTest {

    @Mock
    private UserRepository userRepository;

    private AuthorExistsValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new AuthorExistsValidator(userRepository);
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenUserExists() {
        UUID existingUserId = UUID.randomUUID();
        when(userRepository.existsById(existingUserId)).thenReturn(true);

        boolean isValid = validator.isValid(existingUserId, null);

        assertTrue(isValid);
        verify(userRepository).existsById(existingUserId);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenUserDoesNotExist() {
        UUID nonExistingUserId = UUID.randomUUID();
        when(userRepository.existsById(nonExistingUserId)).thenReturn(false);

        boolean isValid = validator.isValid(nonExistingUserId, null);

        assertFalse(isValid);
        verify(userRepository).existsById(nonExistingUserId);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenUserIdIsNull() {
        boolean isValid = validator.isValid(null, null);

        assertFalse(isValid);
        verifyNoInteractions(userRepository);
    }
}
