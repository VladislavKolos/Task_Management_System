package org.example.tms.exception;

import java.util.UUID;

public class TaskNotFoundException extends TaskManagementSystemException {
    public TaskNotFoundException(UUID id) {
        super("Task not found with ID: " + id);
    }
}
