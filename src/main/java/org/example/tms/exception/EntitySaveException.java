package org.example.tms.exception;

public class EntitySaveException extends TaskManagementSystemException {

    public enum ErrorType {
        COMMENT_SAVE_ERROR,
        TASK_SAVE_ERROR
    }

    public EntitySaveException(ErrorType errorType) {
        super(buildErrorMessage(errorType));
    }

    private static String buildErrorMessage(ErrorType errorType) {
        return switch (errorType) {
            case COMMENT_SAVE_ERROR -> "Failed to save comment.";
            case TASK_SAVE_ERROR -> "Failed to save task.";
        };
    }
}
