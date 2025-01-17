package org.example.tms.exception.custom;

public class TaskAssigneeNotFoundException extends TaskManagementSystemException {

    public TaskAssigneeNotFoundException(String message) {
        super(message);
    }

    public TaskAssigneeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskAssigneeNotFoundException(Throwable cause) {
        super(cause);
    }
}
