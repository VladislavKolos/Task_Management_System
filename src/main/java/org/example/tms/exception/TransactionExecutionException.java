package org.example.tms.exception;

public class TransactionExecutionException extends TaskManagementSystemException {

    public enum ErrorType {
        DATABASE_ERROR_EXECUTING_METHOD,
        IO_ERROR_EXECUTING_METHOD,
        GENERIC_ERROR_EXECUTING_METHOD
    }

    public TransactionExecutionException(ErrorType errorType, String methodName, Throwable ex) {
        super(buildErrorMessage(errorType, methodName, ex));
    }

    private static String buildErrorMessage(ErrorType errorType, String methodName, Throwable ex) {
        return switch (errorType) {
            case DATABASE_ERROR_EXECUTING_METHOD ->
                    "Database error while executing method: " + methodName + " -> " + ex;
            case IO_ERROR_EXECUTING_METHOD -> "I/O error while executing method: " + methodName + " -> " + ex;
            case GENERIC_ERROR_EXECUTING_METHOD -> "Error occurred while executing method: " + methodName + " -> " + ex;
        };
    }
}
