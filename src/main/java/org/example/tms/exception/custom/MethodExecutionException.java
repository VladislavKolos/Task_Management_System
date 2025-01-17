package org.example.tms.exception.custom;

public class MethodExecutionException extends TaskManagementSystemException {

    public MethodExecutionException(String message) {
        super(message);
    }

    public MethodExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodExecutionException(Throwable cause) {
        super(cause);
    }
}
