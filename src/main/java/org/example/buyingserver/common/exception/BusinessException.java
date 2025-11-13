package org.example.buyingserver.common.exception;

import org.example.buyingserver.common.dto.ErrorCode;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;

public class BusinessException extends ApplicationException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}