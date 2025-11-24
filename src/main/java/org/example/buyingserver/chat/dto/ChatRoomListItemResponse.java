package org.example.buyingserver.chat.dto;

public record ChatRoomListItemResponse(
        Long roomId,
        Long postId,
        Long opponentId,
        String opponentName,
        String lastMessage,
        String lastMessageTime,
        int unreadCount
) {}