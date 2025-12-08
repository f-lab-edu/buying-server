package org.example.buyingserver.chat.dto;

import java.util.List;

public record ChatMessagesResponse(
        List<ChatMessageResponse> messages
) {
    public static ChatMessagesResponse from(List<ChatMessageResponse> messages) {
        return new ChatMessagesResponse(messages);
    }
}