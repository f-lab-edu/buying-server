package org.example.buyingserver.chat.event;

public record ChatRoomEnterEvent(
        Long roomId,
        Long memberId
) {}