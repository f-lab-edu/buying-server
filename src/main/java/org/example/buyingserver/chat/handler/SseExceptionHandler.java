package org.example.buyingserver.chat.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

public class SseExceptionHandler {
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<Void> handleSseTimeout(AsyncRequestTimeoutException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    }
