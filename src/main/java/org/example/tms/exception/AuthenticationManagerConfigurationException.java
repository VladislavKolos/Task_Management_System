package org.example.tms.exception;

public class AuthenticationManagerConfigurationException extends TaskManagementSystemException {
    public AuthenticationManagerConfigurationException(Throwable ex) {
        super("Failed to configure AuthenticationManager", ex);
    }
}
