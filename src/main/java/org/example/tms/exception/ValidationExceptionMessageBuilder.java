package org.example.tms.exception;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ValidationExceptionMessageBuilder {
    public String buildValidationErrorMessage(List<FieldError> fieldErrors) {
        StringBuilder errorMessage = new StringBuilder("Validation failed for fields: ");
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField())
                    .append(" (")
                    .append(fieldError.getDefaultMessage())
                    .append("), ");
        }
        errorMessage.setLength(errorMessage.length() - 2);

        return errorMessage.toString();
    }
}
