package org.example.tms.exception.custom;

public class NullUserObjectException extends TaskManagementSystemException {
    public NullUserObjectException(String message) {
        super(message);
    }

    public NullUserObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullUserObjectException(Throwable cause) {
        super(cause);
    }
}
