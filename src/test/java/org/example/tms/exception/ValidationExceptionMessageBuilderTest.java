package org.example.tms.exception;

import org.example.tms.exception.builder.ValidationExceptionMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationExceptionMessageBuilderTest {
    private ValidationExceptionMessageBuilder messageBuilder;

    @BeforeEach
    public void setUp() {
        messageBuilder = new ValidationExceptionMessageBuilder();
    }

    @Test
    public void testBuildValidationErrorMessage_EmptyFieldErrors() {
        List<FieldError> fieldErrors = Collections.emptyList();

        String result = messageBuilder.buildValidationErrorMessage(fieldErrors);

        assertEquals("Validation failed for fields", result);
    }

    @Test
    public void testBuildValidationErrorMessage_SingleFieldError() {
        FieldError fieldError = mock(FieldError.class);
        String fieldName = "email";
        String errorMessage = "must be a valid email";

        when(fieldError.getField()).thenReturn(fieldName);
        when(fieldError.getDefaultMessage()).thenReturn(errorMessage);

        List<FieldError> fieldErrors = Collections.singletonList(fieldError);

        String result = messageBuilder.buildValidationErrorMessage(fieldErrors);

        assertEquals("Validation failed for fields: email (must be a valid email)", result);
    }

    @Test
    public void testBuildValidationErrorMessage_MultipleFieldErrors() {
        FieldError fieldError1 = mock(FieldError.class);
        FieldError fieldError2 = mock(FieldError.class);

        when(fieldError1.getField()).thenReturn("email");
        when(fieldError1.getDefaultMessage()).thenReturn("must be a valid email");

        when(fieldError2.getField()).thenReturn("password");
        when(fieldError2.getDefaultMessage()).thenReturn("must be at least 8 characters");

        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);

        String result = messageBuilder.buildValidationErrorMessage(fieldErrors);

        assertEquals(
                "Validation failed for fields: email (must be a valid email), password (must be at least 8 characters)",
                result);
    }

    @Test
    public void testBuildValidationErrorMessage_WithTrailingComma() {
        FieldError fieldError1 = mock(FieldError.class);
        FieldError fieldError2 = mock(FieldError.class);

        when(fieldError1.getField()).thenReturn("email");
        when(fieldError1.getDefaultMessage()).thenReturn("must be a valid email");

        when(fieldError2.getField()).thenReturn("password");
        when(fieldError2.getDefaultMessage()).thenReturn("must be at least 8 characters");

        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);

        String result = messageBuilder.buildValidationErrorMessage(fieldErrors);

        assertEquals(
                "Validation failed for fields: email (must be a valid email), password (must be at least 8 characters)",
                result);
    }
}