package org.example.buyingserver.chat.dto;

public record ChatRoomRequest(
        Long postId,
        Long buyerId   // 채팅 요청한 사용자
) {}