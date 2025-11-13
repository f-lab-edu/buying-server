package org.example.buyingserver.common.exception;

import lombok.Getter;
import org.example.buyingserver.common.dto.ErrorCode;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}