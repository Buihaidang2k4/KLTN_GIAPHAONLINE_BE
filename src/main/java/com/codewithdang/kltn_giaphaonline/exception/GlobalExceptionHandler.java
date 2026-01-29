package com.codewithdang.kltn_giaphaonline.exception;

import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ============= REQUEST BODY ===================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldErrorResponse>>> handleValidationErrorsLite(
            MethodArgumentNotValidException e) {

        List<FieldErrorResponse> errorResponses = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    String field = fieldError.getField();
                    Object rejectedValue = fieldError.getRejectedValue();
                    String defaultMessage = fieldError.getDefaultMessage();

                    // map enum nếu có
                    ErrorCode errorCode;
                    String message;
                    try {
                        errorCode = ErrorCode.valueOf(defaultMessage);
                        message = errorCode.getMessage();
                    } catch (IllegalArgumentException ex) {
                        errorCode = ErrorCode.INVALID_KEY;
                        // fallback message ngắn gọn
                        message = defaultMessage != null ? defaultMessage : field + " is invalid";
                    }

                    return new FieldErrorResponse(field, rejectedValue, errorCode.getCode(), message);
                })
                .toList();

        ApiResponse<List<FieldErrorResponse>> response = ApiResponse.<List<FieldErrorResponse>>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .data(errorResponses)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    /***
     * @RequestParam , @PathVariable
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(
            ConstraintViolationException e) {

        ConstraintViolation<?> violation =
                e.getConstraintViolations().iterator().next();

        String message = violation.getMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ApiResponse<>(
                        errorCode.getCode(),
                        message,
                        null,
                        LocalDateTime.now()
                ));
    }

    /* ==================== BUSINESS ERROR ==================== */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException e) {

        ErrorCode ec = e.getErrorCode();

        return ResponseEntity
                .status(ec.getStatusCode())
                .body(new ApiResponse<>(
                        ec.getCode(),
                        e.getMessage(),
                        null,
                        LocalDateTime.now()
                ));
    }


    /* ==================== INTERNAL ERROR ==================== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Unhandled exception", e);

        ErrorCode ec = ErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity
                .status(ec.getStatusCode())
                .body(new ApiResponse<>(
                        ec.getCode(),
                        ec.getMessage(),
                        null,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

}
