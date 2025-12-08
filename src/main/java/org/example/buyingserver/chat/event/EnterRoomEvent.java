package org.example.buyingserver.chat.event;

public record EnterRoomEvent (
        Long roomId,
        Long memberId
) {
}