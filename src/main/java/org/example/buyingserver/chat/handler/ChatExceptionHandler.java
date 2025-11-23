package org.example.buyingserver.chat.handler;

import org.example.buyingserver.chat.exception.*;
import org.example.buyingserver.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.buyingserver.chat")
public class ChatExceptionHandler {

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handle(ChatRoomNotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(ChatParticipantNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handle(ChatParticipantNotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(ChatAccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handle(ChatAccessDeniedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(InvalidChatRequestException.class)
    public ResponseEntity<ApiResponse<?>> handle(InvalidChatRequestException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(BuyerConflictWithSellerException.class)
    public ResponseEntity<ApiResponse<?>> handle(BuyerConflictWithSellerException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }
}