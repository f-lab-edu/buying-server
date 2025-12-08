package org.example.buyingserver.chat.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class ChatRoomAlreadyExistsException extends BusinessException {
    public ChatRoomAlreadyExistsException() {
        super(ChatErrorCode.CHATROOM_ALREADY_EXISTS);
    }
}