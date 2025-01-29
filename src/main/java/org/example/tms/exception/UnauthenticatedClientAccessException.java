package org.example.tms.exception;

public class UnauthenticatedClientAccessException extends TaskManagementSystemException {
    public UnauthenticatedClientAccessException() {
        super("Unable to retrieve current User ID, User is not authenticated.");
    }
}
