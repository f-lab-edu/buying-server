package org.example.buyingserver.post.handler;

import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.post.exception.PostNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.buyingserver.post")
public class PostExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePostNotFound(PostNotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }
}