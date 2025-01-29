package org.example.tms.validator.custom;

import org.example.tms.validator.constraint.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TimestampValidatorTest {

    private TimestampValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new TimestampValidator();
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenTimestampIsNull() {
        boolean isValid = validator.isValid(null, null);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenTimestampIsBeforeNow() {
        LocalDateTime timestamp = LocalDateTime.now()
                .minusDays(1);

        boolean isValid = validator.isValid(timestamp, null);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenTimestampIsAfterNow() {
        LocalDateTime timestamp = LocalDateTime.now()
                .plusDays(1);

        boolean isValid = validator.isValid(timestamp, null);

        assertFalse(isValid);
    }
}
