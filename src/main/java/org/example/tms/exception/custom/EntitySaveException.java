package org.example.tms.exception.custom;

public class EntitySaveException extends TaskManagementSystemException {

    public EntitySaveException(String message) {
        super(message);
    }

    public EntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntitySaveException(Throwable cause) {
        super(cause);
    }
}
