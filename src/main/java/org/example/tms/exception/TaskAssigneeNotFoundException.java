package org.example.tms.exception;

import java.util.UUID;

public class TaskAssigneeNotFoundException extends TaskManagementSystemException {

    public TaskAssigneeNotFoundException(UUID assigneeId, UUID taskId) {
        super("User and task not found with IDs: " + assigneeId + " and " + taskId);
    }
}
