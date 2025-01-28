package org.example.tms.exception;

public class InvalidRefreshTokenException extends TaskManagementSystemException {
    public InvalidRefreshTokenException() {
        super("Invalid refresh token");
    }
}
