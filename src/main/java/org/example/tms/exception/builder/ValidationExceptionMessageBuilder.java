package org.example.tms.exception.builder;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * A class for building detailed validation error messages.
 * This class processes a list of field errors and constructs a human-readable message
 * that includes the field names and corresponding error messages.
 * The error message can be used to provide feedback to the User on failed validations.
 */
@Component
public class ValidationExceptionMessageBuilder {

    /**
     * Builds a detailed error message for a list of field errors.
     * The resulting message contains the names of the fields and the corresponding validation error messages.
     * <p>
     * Example message format: "Validation failed for fields: field1 (Error message 1), field2 (Error message 2)"
     *
     * @param fieldErrors a list of `FieldError` objects containing the field name and error message
     * @return a string containing the concatenated error messages for all fields
     */
    public String buildValidationErrorMessage(List<FieldError> fieldErrors) {
        var errorMessage = new StringBuilder("Validation failed for fields: ");
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
