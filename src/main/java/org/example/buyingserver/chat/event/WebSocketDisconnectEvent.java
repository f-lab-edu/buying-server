package org.example.buyingserver.chat.event;

public record WebSocketDisconnectEvent(Long roomId, Long memberId) {}
