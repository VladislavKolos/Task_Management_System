package org.example.tms.exception;

public abstract class TaskManagementSystemException extends RuntimeException {
    public TaskManagementSystemException(String message) {
        super(message);
    }

    public TaskManagementSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskManagementSystemException(Throwable cause) {
        super(cause);
    }
}
