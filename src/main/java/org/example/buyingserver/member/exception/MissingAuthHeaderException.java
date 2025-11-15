package org.example.buyingserver.member.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class MissingAuthHeaderException extends BusinessException {
    public MissingAuthHeaderException() {
        super(MemberErrorCode.MISSING_AUTHORIZATION_HEADER);
    }
}