package org.example.buyingserver.chat.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class ChatRoomNotFoundException extends BusinessException {
    public ChatRoomNotFoundException() {
        super(ChatErrorCode.CHAT_ROOM_NOT_FOUND);
    }
}