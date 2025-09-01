package org.example.tms.exception;

import java.util.UUID;

public class PermissionDeniedException extends TaskManagementSystemException {

    public enum ErrorType {
        USER_NOT_ASSIGNEE_OF_TASK,
        USER_CANNOT_DELETE_COMMENT
    }

    public PermissionDeniedException(ErrorType errorType, UUID id) {
        super(buildErrorMessage(errorType, id));
    }

    public PermissionDeniedException(String message) {
        super(message);
    }


    private static String buildErrorMessage(ErrorType errorType, UUID id) {
        return switch (errorType) {
            case USER_NOT_ASSIGNEE_OF_TASK -> "User with ID: " + id + " - is not an assignee of the task.";
            case USER_CANNOT_DELETE_COMMENT ->
                    "User with ID: " + id + " - does not have permission to delete this comment.";
        };
    }
}
