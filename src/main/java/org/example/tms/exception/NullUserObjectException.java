package org.example.tms.exception;

public class NullUserObjectException extends TaskManagementSystemException {
    public NullUserObjectException() {
        super("Unable to retrieve current User ID, User object is null.");
    }
}
