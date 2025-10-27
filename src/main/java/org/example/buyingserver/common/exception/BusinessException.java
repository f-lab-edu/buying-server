package org.example.buyingserver.common.exception;

import org.example.buyingserver.common.dto.ErrorCodeAndMessage;

public class BusinessException extends ApplicationException {
    public BusinessException(ErrorCodeAndMessage errorCodeAndMessage) {
        super(errorCodeAndMessage);
    }
}