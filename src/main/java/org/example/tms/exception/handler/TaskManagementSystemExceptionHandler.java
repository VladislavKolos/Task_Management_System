package org.example.tms.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.ApiException;
import org.example.tms.exception.ValidationExceptionMessageBuilder;
import org.example.tms.exception.custom.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class TaskManagementSystemExceptionHandler {

    @Value("${app.support.email}")
    private String supportEmail;
    private final ValidationExceptionMessageBuilder validationExceptionMessageBuilder;

    @ExceptionHandler(MethodExecutionException.class)
    public ResponseEntity<ApiException> handleMethodExecutionException(
            MethodExecutionException ex, WebRequest request) {

        String message = "An error occurred during method execution.";

        log.error("Method execution failed at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new MethodExecutionException(message), HttpStatus.INTERNAL_SERVER_ERROR,
                getRequestPath(request), "Method Execution Error");
    }

    @ExceptionHandler(EntitySaveException.class)
    public ResponseEntity<ApiException> handleEntitySaveException(
            EntitySaveException ex, WebRequest request) {

        String message = "Unable to save your object at the moment. Please try again later.";

        log.error("Failed to save object at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new EntitySaveException(message), HttpStatus.INTERNAL_SERVER_ERROR,
                getRequestPath(request),
                "Entity Save Error");
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ApiException> handlePermissionDeniedException(
            PermissionDeniedException ex, WebRequest request) {

        String message = "You do not have permission to perform this action.";

        log.warn("Permission denied at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new PermissionDeniedException(message), HttpStatus.FORBIDDEN,
                getRequestPath(request),
                "Access Denied");
    }

    @ExceptionHandler(AuthenticationManagerConfigurationException.class)
    public ResponseEntity<ApiException> handleAuthenticationManagerConfigurationException(
            AuthenticationManagerConfigurationException ex, WebRequest request) {

        String message = "An unexpected error occurred. Please contact support: " + supportEmail;

        log.error("Error at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new AuthenticationManagerConfigurationException(message),
                HttpStatus.INTERNAL_SERVER_ERROR, getRequestPath(request),
                "Unexpected Error");
    }

    @ExceptionHandler(BlacklistedTokenAccessDeniedException.class)
    public ResponseEntity<ApiException> handleBlacklistedTokenAccessDeniedException(
            BlacklistedTokenAccessDeniedException ex, WebRequest request) {

        String message = "Your session is no longer valid. Please login again.";

        log.warn("Blacklisted token access attempt at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new BlacklistedTokenAccessDeniedException(message), HttpStatus.FORBIDDEN,
                getRequestPath(request),
                "Invalid Session");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiException> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException ex, WebRequest request) {

        String message = "Session expired or invalid. Please login again.";

        log.warn("Invalid refresh token used at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new InvalidRefreshTokenException(message), HttpStatus.UNAUTHORIZED,
                getRequestPath(request),
                "Session Error");
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ApiException> handleJwtAuthenticationException(
            JwtAuthenticationException ex, WebRequest request) {

        String message = "Authentication failed. Please try again or contact support: " + supportEmail;

        log.error("Authentication error at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new JwtAuthenticationException(message), HttpStatus.UNAUTHORIZED,
                getRequestPath(request),
                "Authentication Error");
    }

    @ExceptionHandler(NullUserObjectException.class)
    public ResponseEntity<ApiException> handleNullUserObjectException(
            NullUserObjectException ex, WebRequest request) {

        String message = "The user object is null. Please ensure all required data is provided.";

        log.error("Null user object encountered at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new NullUserObjectException(message), HttpStatus.INTERNAL_SERVER_ERROR,
                getRequestPath(request),
                "Unexpected Error");
    }

    @ExceptionHandler(SecurityFilterConfigurationException.class)
    public ResponseEntity<ApiException> handleSecurityFilterConfigurationException(
            SecurityFilterConfigurationException ex, WebRequest request) {

        String message = "An unexpected error occurred. Please contact support: " + supportEmail;

        log.error("Security filter configuration error at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new SecurityFilterConfigurationException(message),
                HttpStatus.INTERNAL_SERVER_ERROR, getRequestPath(request),
                "Unexpected Error");
    }

    @ExceptionHandler(UnauthenticatedClientAccessException.class)
    public ResponseEntity<ApiException> handleUnauthenticatedClientAccessException(
            UnauthenticatedClientAccessException ex, WebRequest request) {

        String message = "You need to log in to access this resource.";

        log.warn("Unauthenticated access attempt at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new UnauthenticatedClientAccessException(message), HttpStatus.UNAUTHORIZED,
                getRequestPath(request),
                "Access Denied");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiException> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {

        String message = "The requested information was not found.";

        log.warn("User not found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new UserNotFoundException(message), HttpStatus.NOT_FOUND, getRequestPath(request),
                "Resource Not Found");
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiException> handleTaskNotFoundException(
            TaskNotFoundException ex, WebRequest request) {

        String message = "The requested information was not found.";

        log.warn("Task not found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new TaskNotFoundException(message), HttpStatus.NOT_FOUND, getRequestPath(request),
                "Resource Not Found");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiException> handleCommentNotFoundException(
            CommentNotFoundException ex, WebRequest request) {

        String message = "The requested information was not found.";

        log.warn("Comment not found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new CommentNotFoundException(message), HttpStatus.NOT_FOUND,
                getRequestPath(request),
                "Resource Not Found");
    }

    @ExceptionHandler(TaskAssigneeNotFoundException.class)
    public ResponseEntity<ApiException> handleTaskAssigneeNotFoundException(
            TaskAssigneeNotFoundException ex, WebRequest request) {

        String message = "The requested information was not found.";

        log.warn("TaskAssignee not found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new TaskAssigneeNotFoundException(message), HttpStatus.NOT_FOUND,
                getRequestPath(request),
                "Resource Not Found");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException> handleGeneralException(Exception ex, WebRequest request) {
        String message = "An unexpected error occurred. Please try again later or contact support: " + supportEmail;

        log.error("Unexpected error occurred at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new Exception(message), HttpStatus.INTERNAL_SERVER_ERROR, getRequestPath(request),
                "Unexpected Error");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiException> handleRuntimeException(RuntimeException ex, WebRequest request) {
        String message = "A runtime error occurred. Please try again later or contact support: " + supportEmail;

        log.error("Runtime exception occurred at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new RuntimeException(message), HttpStatus.INTERNAL_SERVER_ERROR,
                getRequestPath(request), "Runtime Error");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            String errorMessage = validationExceptionMessageBuilder.buildValidationErrorMessage(fieldErrors);

            log.warn("Validation error at [{}]: {}", getRequestPath(request), errorMessage);

            return buildExceptionResponse(new Exception(errorMessage), HttpStatus.BAD_REQUEST, getRequestPath(request),
                    "Validation Error");
        }
        String message = "Validation failed. Ensure all fields are correctly filled.";

        log.warn("Constraint violation occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new Exception(message), HttpStatus.BAD_REQUEST, getRequestPath(request),
                "Validation Error");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiException> handleConstraintViolationException(ConstraintViolationException ex,
                                                                           WebRequest request) {
        String message = "Validation failed. Ensure all fields are correctly filled.";

        log.warn("Constraint violation occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new Exception(message), HttpStatus.BAD_REQUEST, getRequestPath(request),
                "Validation Error");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiException> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        String message = "Operation cannot be completed due to the current system state. Please check the data or try again later.";

        log.error("Illegal state exception at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new Exception(message), HttpStatus.FORBIDDEN, getRequestPath(request),
                "Illegal State");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiException> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        String message = "The requested resource was not found.";

        log.warn("No such element found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new Exception(message), HttpStatus.NOT_FOUND, getRequestPath(request),
                "Resource Not Found");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiException> handleNullPointerException(NullPointerException ex, WebRequest request) {
        String message = "An internal error occurred. Please try again later.";

        log.error("Null pointer exception at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(new Exception(message), HttpStatus.INTERNAL_SERVER_ERROR, getRequestPath(request),
                "Internal Error");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiException> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                       WebRequest request) {
        String message = "Invalid input provided. Please verify the data and try again.";

        log.warn("Illegal argument exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new Exception(message), HttpStatus.BAD_REQUEST, getRequestPath(request),
                "Invalid Argument");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiException> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        String message = "Your session has expired. Please log in again to continue.";

        log.warn("Expired JWT exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new Exception(message), HttpStatus.UNAUTHORIZED, getRequestPath(request),
                "Session Expired");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiException> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                       WebRequest request) {
        String message = "The requested resource could not be found. Please check your request and try again.";

        log.warn("No resource found exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(new Exception(message), HttpStatus.NOT_FOUND, getRequestPath(request),
                "Resource Not Found");
    }

    private String getRequestPath(WebRequest request) {
        return request.getDescription(false)
                .replace("uri=", "");
    }

    private ResponseEntity<ApiException> buildExceptionResponse(Exception ex, HttpStatus status, String path,
                                                                String exceptionType) {
        log.error("Error occurred: {} at path: {}", ex.getMessage(), path, ex);

        ApiException apiException = ApiException.builder()
                .status(status.value())
                .message(ex.getMessage())
                .error(exceptionType)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiException, status);
    }
}
