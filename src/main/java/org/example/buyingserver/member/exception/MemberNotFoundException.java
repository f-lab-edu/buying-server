package org.example.buyingserver.member.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}