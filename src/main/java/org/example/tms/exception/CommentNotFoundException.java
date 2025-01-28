package org.example.tms.exception;

import java.util.UUID;

public class CommentNotFoundException extends TaskManagementSystemException {

    public CommentNotFoundException(UUID id) {
        super("Comment not found with ID: " + id);
    }
}
