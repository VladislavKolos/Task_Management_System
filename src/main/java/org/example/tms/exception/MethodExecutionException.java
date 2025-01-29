package org.example.tms.exception;

public class MethodExecutionException extends TaskManagementSystemException {
    public MethodExecutionException(String methodName, Throwable ex) {
        super("Execution of method '" + methodName + "' failed: " + ex);
    }
}
