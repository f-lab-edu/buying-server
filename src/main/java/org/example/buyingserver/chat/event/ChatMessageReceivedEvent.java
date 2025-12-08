package org.example.buyingserver.chat.event;

import org.example.buyingserver.chat.domain.ChatMessage;

public record ChatMessageReceivedEvent(
        ChatMessage message
) {}