package org.example.buyingserver.member.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException() {
        super(MemberErrorCode.INVALID_PASSWORD);
    }
}