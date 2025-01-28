package org.example.tms.exception;

import java.util.UUID;

public class UserNotFoundException extends TaskManagementSystemException {
    public UserNotFoundException(UUID id) {
        super("User not found with ID: " + id);
    }

    public UserNotFoundException(String email) {
        super("User with this email: " + email + " was not found in database");
    }
}
