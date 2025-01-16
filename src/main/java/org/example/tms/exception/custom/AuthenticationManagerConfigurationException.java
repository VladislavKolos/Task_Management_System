package org.example.tms.exception.custom;

public class AuthenticationManagerConfigurationException extends TaskManagementSystemException {
    public AuthenticationManagerConfigurationException(String message) {
        super(message);
    }

    public AuthenticationManagerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationManagerConfigurationException(Throwable cause) {
        super(cause);
    }
}
