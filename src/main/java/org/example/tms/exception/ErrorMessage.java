package org.example.tms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    METHOD_EXECUTION_ERROR("ERR001", "An error occurred during method execution."),
    ENTITY_SAVE_ERROR("ERR002", "Unable to save your object at the moment. Please try again later."),
    PERMISSION_DENIED_ERROR("ERR003", "You do not have permission to perform this action."),
    AUTHENTICATION_MANAGER_CONFIGURATION_ERROR("ERR004",
            "An unexpected error occurred. Please contact support: support@tms.com"),
    BLACKLISTED_TOKEN_ACCESS_DENIED_ERROR("ERR005", "Your session is no longer valid. Please login again."),
    INVALID_REFRESH_TOKEN_ERROR("ERR006", "Session expired or invalid. Please login again."),
    JWT_AUTHENTICATION_ERROR("ERR007", "Authentication failed. Please try again or contact support: support@tms.com"),
    NULL_USER_OBJECT_ERROR("ERR008", "The user object is null. Please ensure all required data is provided."),
    SECURITY_FILTER_CONFIGURATION_ERROR("ERR009",
            "An unexpected error occurred. Please contact support: support@tms.com"),
    UNAUTHENTICATED_CLIENT_ACCESS_ERROR("ERR010", "You need to log in to access this resource."),
    RESOURCE_NOT_FOUND_ERROR("ERR011", "The requested information was not found."),
    METHOD_ARGUMENT_NOT_VALID_ERROR("ERR012", "Validation failed. Ensure all fields are correctly filled."),
    RUNTIME_ERROR("ERR013", "A runtime error occurred. Please try again later or contact support: support@tms.com"),
    ERROR("ERR014", "An unexpected error occurred. Please try again later or contact support: support@tms.com"),
    CONSTRAINT_VIOLATION_ERROR("ERR015", "Validation failed. Ensure all fields are correctly filled."),
    ILLEGAL_STATE_ERROR("ERR016",
            "Operation cannot be completed due to the current system state. Please check the data or try again later."),
    NO_SUCH_ELEMENT_ERROR("ERR017", "The requested resource was not found."),
    NULL_POINTER_ERROR("ERR018", "An internal error occurred. Please try again later."),
    ILLEGAL_ARGUMENT_ERROR("ERR019", "Invalid input provided. Please verify the data and try again."),
    EXPIRED_JWT_ERROR("ERR020", "Your session has expired. Please log in again to continue."),
    NO_RESOURCE_FOUND_ERROR("ERR021",
            "The requested resource could not be found. Please check your request and try again.");

    private final String code;
    private final String message;
}
