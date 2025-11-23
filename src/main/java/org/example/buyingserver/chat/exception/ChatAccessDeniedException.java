package org.example.buyingserver.chat.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class ChatAccessDeniedException extends BusinessException {
    public ChatAccessDeniedException() {
        super(ChatErrorCode.CHAT_ACCESS_DENIED);
    }
}