package org.example.tms.exception;

public class SecurityFilterConfigurationException extends TaskManagementSystemException {
    public SecurityFilterConfigurationException(Throwable ex) {
        super("Error configuring security filter chain", ex);
    }
}
