package org.example.buyingserver.chat.event;

import java.util.List;

public record MessageSavedEvent(
        Long roomId,
        Long writerId,
        String messageId,
        String content,
        List<Long> participantIds

) {
}