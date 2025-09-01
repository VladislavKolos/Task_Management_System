package org.example.tms.exception;

public class InvalidRefreshTokenException extends TaskManagementSystemException {
    public InvalidRefreshTokenException(String refreshToken) {
        super("Invalid refresh token: " + refreshToken);
    }
}
