package org.example.buyingserver.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeAndMessage implements ErrorCode {

    // 4xx - 클라이언트 오류
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT.value(), "리소스 상태가 충돌했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "입력값이 유효하지 않습니다."),
    GOOGLE_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST.value(), "구글 OAuth 토큰 발급에 실패했습니다."),
    GOOGLE_PROFILE_REQUEST_FAILED(HttpStatus.BAD_REQUEST.value(), "구글 사용자 프로필 정보를 가져오지 못했습니다."),

    // 5xx - 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "이미 등록된 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "비밀번호가 일치하지 않습니다."),

    //  인증/토큰 관련
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORMAT(401, "토큰 형식이 올바르지 않습니다."),
    MISSING_AUTHORIZATION_HEADER(401, "Authorization 헤더가 존재하지 않습니다.");

    private final int code;
    private final String message;

    ErrorCodeAndMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}