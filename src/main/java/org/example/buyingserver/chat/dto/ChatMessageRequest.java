package org.example.buyingserver.chat.dto;

public record ChatMessageRequest(
        Long senderId,
        String content ) {
}