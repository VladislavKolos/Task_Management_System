package org.example.tms.exception.custom;

public class SecurityFilterConfigurationException extends TaskManagementSystemException {
    public SecurityFilterConfigurationException(String message) {
        super(message);
    }

    public SecurityFilterConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityFilterConfigurationException(Throwable cause) {
        super(cause);
    }
}
