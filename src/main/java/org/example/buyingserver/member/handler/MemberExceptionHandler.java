package org.example.buyingserver.member.handler;

import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.member.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.buyingserver.member")
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handle(MemberNotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<?>> handle(InvalidPasswordException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<?>> handle(DuplicateEmailException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(MissingAuthHeaderException.class)
    public ResponseEntity<ApiResponse<?>> handle(MissingAuthHeaderException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }
}