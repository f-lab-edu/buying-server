package org.example.buyingserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "fail")
public class ErrorResponse {
    private final String message;
    private final int code;

    public static ErrorResponse fail(ErrorCode errorCodeAndMessage) {
        return new ErrorResponse(
                errorCodeAndMessage.getMessage(),
                errorCodeAndMessage.getCode()
        );
    }
}