package org.example.buyingserver.chat.event;

public record RoomCreatedEvent(
        Long buyerId,
        Long sellerId,
        Long roomId
) {
}
