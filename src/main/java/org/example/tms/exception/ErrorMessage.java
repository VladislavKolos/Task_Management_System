package org.example.tms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    METHOD_EXECUTION_ERROR("An error occurred during method execution.", HttpStatus.INTERNAL_SERVER_ERROR),
    ENTITY_SAVE_ERROR("Unable to save your object at the moment. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    PERMISSION_DENIED_ERROR("You do not have permission to perform this action.", HttpStatus.FORBIDDEN),
    AUTHENTICATION_MANAGER_CONFIGURATION_ERROR("An unexpected error occurred. Please contact support: support@tms.com",
            HttpStatus.INTERNAL_SERVER_ERROR),
    BLACKLISTED_TOKEN_ACCESS_DENIED_ERROR("Your session is no longer valid. Please login again.", HttpStatus.FORBIDDEN),
    INVALID_REFRESH_TOKEN_ERROR("Session expired or invalid. Please login again.", HttpStatus.UNAUTHORIZED),
    JWT_AUTHENTICATION_ERROR("Authentication failed. Please try again or contact support: support@tms.com",
            HttpStatus.UNAUTHORIZED),
    NULL_USER_OBJECT_ERROR("The user object is null. Please ensure all required data is provided.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    SECURITY_FILTER_CONFIGURATION_ERROR("An unexpected error occurred. Please contact support: support@tms.com",
            HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED_CLIENT_ACCESS_ERROR("You need to log in to access this resource.", HttpStatus.UNAUTHORIZED),
    RESOURCE_NOT_FOUND_ERROR("The requested information was not found.", HttpStatus.NOT_FOUND),
    METHOD_ARGUMENT_NOT_VALID_ERROR("Validation failed. Ensure all fields are correctly filled.",
            HttpStatus.BAD_REQUEST),
    RUNTIME_ERROR("A runtime error occurred. Please try again later or contact support: support@tms.com",
            HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR("An unexpected error occurred. Please try again later or contact support: support@tms.com",
            HttpStatus.INTERNAL_SERVER_ERROR),
    CONSTRAINT_VIOLATION_ERROR("Validation failed. Ensure all fields are correctly filled.", HttpStatus.BAD_REQUEST),
    ILLEGAL_STATE_ERROR(
            "Operation cannot be completed due to the current system state. Please check the data or try again later.",
            HttpStatus.FORBIDDEN),
    NO_SUCH_ELEMENT_ERROR("The requested resource was not found.", HttpStatus.NOT_FOUND),
    NULL_POINTER_ERROR("An internal error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_ARGUMENT_ERROR("Invalid input provided. Please verify the data and try again.", HttpStatus.BAD_REQUEST),
    EXPIRED_JWT_ERROR("Your session has expired. Please log in again to continue.", HttpStatus.UNAUTHORIZED),
    NO_RESOURCE_FOUND_ERROR("The requested resource could not be found. Please check your request and try again.",
            HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;
}
