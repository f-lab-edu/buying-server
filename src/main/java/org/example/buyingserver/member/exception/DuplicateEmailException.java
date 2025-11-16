package org.example.buyingserver.member.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(MemberErrorCode.DUPLICATE_EMAIL);
    }
}