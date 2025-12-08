package org.example.buyingserver.chat.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class InvalidChatRequestException extends BusinessException {
    public InvalidChatRequestException() {
        super(ChatErrorCode.INVALID_CHAT_REQUEST);
    }
}