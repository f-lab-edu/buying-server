package org.example.buyingserver.post.handler;

import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.post.exception.PostAlreadyDeletedException;
import org.example.buyingserver.post.exception.PostAlreadyReservedException;
import org.example.buyingserver.post.exception.PostNotFoundException;
import org.example.buyingserver.post.exception.PostNotReservedException;
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

    @ExceptionHandler(PostAlreadyDeletedException.class)
    public ResponseEntity<ApiResponse<?>> handlePostAlreadyDeleted(PostAlreadyDeletedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(PostAlreadyReservedException.class)
    public ResponseEntity<ApiResponse<?>> handlePostAlreadyReserved(PostAlreadyReservedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(PostNotReservedException.class)
    public ResponseEntity<ApiResponse<?>> handlePostNotReserved(PostNotReservedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }
}