package org.example.buyingserver.common.exception;

import org.example.buyingserver.common.exception.GlobalErrorCode;

public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(GlobalErrorCode.FORBIDDEN);
    }
}
