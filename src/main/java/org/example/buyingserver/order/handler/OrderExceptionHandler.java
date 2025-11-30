package org.example.buyingserver.order.handler;

import org.example.buyingserver.order.exception.OrderNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.example.buyingserver.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice(basePackages = "org.example.buyingserver.order")
public class OrderExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleOrderException(OrderNotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }
}