package org.example.tms.exception.custom;

public class CommentNotFoundException extends TaskManagementSystemException {

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }
}
