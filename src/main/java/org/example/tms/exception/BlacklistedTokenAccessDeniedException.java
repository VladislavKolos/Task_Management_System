package org.example.tms.exception;

public class BlacklistedTokenAccessDeniedException extends TaskManagementSystemException {
    public BlacklistedTokenAccessDeniedException(Throwable ex) {
        super("Error while denying access due to blacklisted token", ex);
    }
}
