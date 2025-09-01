package org.example.tms.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.*;
import org.example.tms.exception.builder.ValidationExceptionMessageBuilder;
import org.example.tms.exception.dto.ApiExceptionDto;
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
    private final ValidationExceptionMessageBuilder validationExceptionMessageBuilder;

    @ExceptionHandler(MethodExecutionException.class)
    public ResponseEntity<ApiExceptionDto> handleMethodExecutionException(
            MethodExecutionException ex, WebRequest request) {
        log.error("Method execution failed at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.METHOD_EXECUTION_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(EntitySaveException.class)
    public ResponseEntity<ApiExceptionDto> handleEntitySaveException(
            EntitySaveException ex, WebRequest request) {
        log.error("Failed to save object at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.ENTITY_SAVE_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ApiExceptionDto> handlePermissionDeniedException(
            PermissionDeniedException ex, WebRequest request) {
        log.warn("Permission denied at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.PERMISSION_DENIED_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(AuthenticationManagerConfigurationException.class)
    public ResponseEntity<ApiExceptionDto> handleAuthenticationManagerConfigurationException(
            AuthenticationManagerConfigurationException ex, WebRequest request) {
        log.error("Error at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.AUTHENTICATION_MANAGER_CONFIGURATION_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(BlacklistedTokenAccessDeniedException.class)
    public ResponseEntity<ApiExceptionDto> handleBlacklistedTokenAccessDeniedException(
            BlacklistedTokenAccessDeniedException ex, WebRequest request) {
        log.warn("Blacklisted token access attempt at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.BLACKLISTED_TOKEN_ACCESS_DENIED_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiExceptionDto> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException ex, WebRequest request) {
        log.warn("Invalid refresh token used at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.INVALID_REFRESH_TOKEN_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ApiExceptionDto> handleJwtAuthenticationException(
            JwtAuthenticationException ex, WebRequest request) {
        log.error("Authentication error at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.JWT_AUTHENTICATION_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NullUserObjectException.class)
    public ResponseEntity<ApiExceptionDto> handleNullUserObjectException(
            NullUserObjectException ex, WebRequest request) {
        log.error("Null user object encountered at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.NULL_USER_OBJECT_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(SecurityFilterConfigurationException.class)
    public ResponseEntity<ApiExceptionDto> handleSecurityFilterConfigurationException(
            SecurityFilterConfigurationException ex, WebRequest request) {
        log.error("Security filter configuration error at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.SECURITY_FILTER_CONFIGURATION_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(UnauthenticatedClientAccessException.class)
    public ResponseEntity<ApiExceptionDto> handleUnauthenticatedClientAccessException(
            UnauthenticatedClientAccessException ex, WebRequest request) {
        log.warn("Unauthenticated access attempt at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.UNAUTHENTICATED_CLIENT_ACCESS_ERROR, getRequestPath(request));
    }

    @ExceptionHandler({UserNotFoundException.class,
            TaskNotFoundException.class,
            CommentNotFoundException.class,
            TaskAssigneeNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleResourceNotFoundException(
            RuntimeException ex, WebRequest request) {
        log.warn("User not found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.RESOURCE_NOT_FOUND_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDto> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.ERROR, getRequestPath(request));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiExceptionDto> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime exception occurred at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);
        return buildExceptionResponse(ErrorMessage.RUNTIME_ERROR, getRequestPath(request));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                  WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            String errorMessage = validationExceptionMessageBuilder.buildValidationErrorMessage(fieldErrors);
            log.warn("Validation error at [{}]: {}", getRequestPath(request), errorMessage);

            return buildExceptionResponse(ErrorMessage.METHOD_ARGUMENT_NOT_VALID_ERROR.getHttpStatus(),
                    getRequestPath(request), errorMessage);
        }
        log.warn("Constraint violation occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.METHOD_ARGUMENT_NOT_VALID_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiExceptionDto> handleConstraintViolationException(ConstraintViolationException ex,
                                                                              WebRequest request) {
        log.warn("Constraint violation occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.CONSTRAINT_VIOLATION_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiExceptionDto> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.error("Illegal state exception at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.ILLEGAL_STATE_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiExceptionDto> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        log.warn("No such element found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.NO_SUCH_ELEMENT_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiExceptionDto> handleNullPointerException(NullPointerException ex, WebRequest request) {
        log.error("Null pointer exception at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.NULL_POINTER_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiExceptionDto> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                          WebRequest request) {

        log.warn("Illegal argument exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.ILLEGAL_ARGUMENT_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiExceptionDto> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        log.warn("Expired JWT exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.EXPIRED_JWT_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                          WebRequest request) {

        log.warn("No resource found exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.NO_RESOURCE_FOUND_ERROR, getRequestPath(request));
    }

    private String getRequestPath(WebRequest request) {
        return request.getDescription(false)
                .replace("uri=", "");
    }

    private ResponseEntity<ApiExceptionDto> buildExceptionResponse(ErrorMessage errorMessage, String path) {
        return buildExceptionResponse(errorMessage.getHttpStatus(), path, errorMessage.getMessage());
    }

    private ResponseEntity<ApiExceptionDto> buildExceptionResponse(HttpStatus status,
                                                                   String path, String customMessage) {
        log.error("Error occurred: {} at path: {}", customMessage, path);

        ApiExceptionDto apiException = ApiExceptionDto.builder()
                .status(status.value())
                .message(customMessage)
                .error(status.getReasonPhrase())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiException, status);
    }
}
