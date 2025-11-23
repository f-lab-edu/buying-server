package org.example.buyingserver.chat.dto;

import org.example.buyingserver.chat.domain.ChatMessage;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record ChatMessageResponse(
        String id,
        Long roomId,
        Long writerId,
        String content,
        String messageType,
        List<String> attachments,
        Set<Long> readBy,
        Instant createdAt
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getRoomId(),
                message.getWriterId(),
                message.getContent(),
                message.getMessageType().name(),
                message.getAttachments(),
                message.getReadBy(),
                message.getCreatedAt()
        );
    }
}