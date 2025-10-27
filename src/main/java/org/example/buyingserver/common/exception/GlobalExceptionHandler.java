package org.example.buyingserver.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("[BusinessException] {}: {}", e.getErrorCodeAndMessage(), e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.valueOf(e.getErrorCodeAndMessage().getCode()))
                .body(ErrorResponse.fail(e.getErrorCodeAndMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("[ValidationException] {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.fail(ErrorCodeAndMessage.INVALID_INPUT));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("[UnexpectedException] {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.fail(ErrorCodeAndMessage.INTERNAL_SERVER_ERROR));
    }
}
