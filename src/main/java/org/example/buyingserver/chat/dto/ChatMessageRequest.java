package org.example.buyingserver.chat.dto;

public record ChatMessageRequest(
        Long writerId,
        String content ) {
}