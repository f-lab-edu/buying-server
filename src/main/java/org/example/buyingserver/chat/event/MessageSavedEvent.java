package org.example.buyingserver.chat.event;

public record MessageSavedEvent(
        Long roomId,
        Long writerId,
        String content
) {
}