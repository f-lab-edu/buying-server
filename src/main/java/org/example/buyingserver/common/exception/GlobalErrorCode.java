package org.example.buyingserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    // 4xx - 클라이언트 오류 (1000번대)
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 1000, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 1001, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), 1002, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1003, "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT.value(), 1004, "리소스 상태가 충돌했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), 1005, "입력값이 유효하지 않습니다."),
    GOOGLE_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST.value(), 1006, "구글 OAuth 토큰 발급에 실패했습니다."),
    GOOGLE_PROFILE_REQUEST_FAILED(HttpStatus.BAD_REQUEST.value(), 1007, "구글 사용자 프로필 정보를 가져오지 못했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), 1008, "접근 권한이 없습니다."),

    // 5xx - 서버 오류 (2000번대)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 2000, "서버 내부 오류가 발생했습니다."),

    // 인증/토큰 관련 (3000번대)
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), 3000, "만료된 토큰입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(), 3001, "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED.value(), 3002, "토큰 형식이 올바르지 않습니다."),
    MISSING_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED.value(), 3003, "Authorization 헤더가 존재하지 않습니다.");

    private final int status;
    private final int code;
    private final String message;
}
