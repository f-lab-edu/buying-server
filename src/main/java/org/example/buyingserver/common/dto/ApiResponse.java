package org.example.buyingserver.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private String message;
    private int code;
    private T data;

    private ApiResponse(String message, int code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(ResponseCodeAndMessage responseCodeAndMessage, T data) {
        return new ApiResponse<>(responseCodeAndMessage.getMessage(), responseCodeAndMessage.getCode(), data);
    }

    public static <T> ApiResponse<T> success(ResponseCodePostAndMessage responseCodeAndMessage, T data) {
        return new ApiResponse<>(responseCodeAndMessage.getMessage(), responseCodeAndMessage.getCode(), data);
    }

    public static <T> ApiResponse<T> noContent(ResponseCodeAndMessage responseCodeAndMessage) {
        return new ApiResponse<>(responseCodeAndMessage.getMessage(), responseCodeAndMessage.getCode(), null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getMessage(), errorCode.getCode(), null);
    }
}
