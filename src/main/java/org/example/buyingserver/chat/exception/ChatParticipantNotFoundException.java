package org.example.buyingserver.chat.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class ChatParticipantNotFoundException extends BusinessException {
    public ChatParticipantNotFoundException() {
        super(ChatErrorCode.PARTICIPANT_NOT_FOUND);
    }
}