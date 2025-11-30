package org.example.buyingserver.payment.handler;

import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.payment.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.buyingserver.payment")
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(PaymentNotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(PaymentNotReadyException.class)
    public ResponseEntity<ApiResponse<?>> handleNotReady(PaymentNotReadyException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(PaymentNotDoneException.class)
    public ResponseEntity<ApiResponse<?>> handleNotDone(PaymentNotDoneException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(PaymentAlreadyDoneException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyDone(PaymentAlreadyDoneException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(PaymentFailedStateException.class)
    public ResponseEntity<ApiResponse<?>> handleFailedState(PaymentFailedStateException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }
}