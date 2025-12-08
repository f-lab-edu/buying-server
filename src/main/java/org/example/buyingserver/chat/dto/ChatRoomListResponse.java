package org.example.buyingserver.chat.dto;

import java.util.List;

public record ChatRoomListResponse(
        List<ChatRoomListItemResponse> rooms
) {
    public static ChatRoomListResponse from(List<ChatRoomListItemResponse> list) {
        return new ChatRoomListResponse(list);
    }
}

