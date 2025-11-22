package org.example.buyingserver.chat.dto;

public record ChatMessageRequest(
        String senderId,
        String message
) {
}