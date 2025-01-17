package org.example.tms.exception.custom;

public class TransactionExecutionException extends TaskManagementSystemException {

    public TransactionExecutionException(String message) {
        super(message);
    }

    public TransactionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionExecutionException(Throwable cause) {
        super(cause);
    }
}
