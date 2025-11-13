package org.example.buyingserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 오류 응답을 위한 공통 응답 클래스
 */
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