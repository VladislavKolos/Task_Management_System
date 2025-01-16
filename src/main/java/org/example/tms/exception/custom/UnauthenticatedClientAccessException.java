package org.example.tms.exception.custom;

public class UnauthenticatedClientAccessException extends TaskManagementSystemException {
    public UnauthenticatedClientAccessException(String message) {
        super(message);
    }

    public UnauthenticatedClientAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthenticatedClientAccessException(Throwable cause) {
        super(cause);
    }
}
