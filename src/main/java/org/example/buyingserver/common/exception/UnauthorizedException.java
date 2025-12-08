package org.example.buyingserver.common.exception;

import org.example.buyingserver.common.exception.GlobalErrorCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException() {
        super(GlobalErrorCode.UNAUTHORIZED);
    }
}