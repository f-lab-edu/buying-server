package org.example.buyingserver.common.exception;

import lombok.Getter;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCodeAndMessage errorCodeAndMessage;

    public ApplicationException(ErrorCodeAndMessage errorCodeAndMessage) {
        super(errorCodeAndMessage.getMessage());
        this.errorCodeAndMessage = errorCodeAndMessage;
    }
}