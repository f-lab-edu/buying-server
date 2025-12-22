package org.example.buyingserver.order.exception;

import org.example.buyingserver.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.buyingserver.order")
public class OrderExceptionHandler {
        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleOrderException(OrderNotFoundException e) {
            return ResponseEntity
                    .status(e.getErrorCode().getStatus())
                    .body(ApiResponse.error(e.getErrorCode()));
        }

        @ExceptionHandler(OrderNotReadyException.class)
        public ResponseEntity<ApiResponse<?>> handleOrderNotReady(OrderNotReadyException e) {
            return ResponseEntity
                    .status(e.getErrorCode().getStatus())
                    .body(ApiResponse.error(e.getErrorCode()));
        }
}