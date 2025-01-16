package org.example.tms.exception.custom;

public class BlacklistedTokenAccessDeniedException extends TaskManagementSystemException {
    public BlacklistedTokenAccessDeniedException(String message) {
        super(message);
    }

    public BlacklistedTokenAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlacklistedTokenAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
