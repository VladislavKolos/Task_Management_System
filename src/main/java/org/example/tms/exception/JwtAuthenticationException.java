package org.example.tms.exception;

public class JwtAuthenticationException extends TaskManagementSystemException {

    public enum ErrorType {
        JWT_AUTH_IO_ERROR,
        JWT_AUTH_UNEXPECTED_ERROR
    }

    public JwtAuthenticationException(ErrorType errorType, Throwable ex) {
        super(buildErrorMessage(errorType, ex));
    }

    private static String buildErrorMessage(ErrorType errorType, Throwable ex) {
        return switch (errorType) {
            case JWT_AUTH_IO_ERROR -> "I/O Error during JWT authentication: " + ex;
            case JWT_AUTH_UNEXPECTED_ERROR -> "Unexpected error during JWT authentication: " + ex;
        };
    }
}
