package org.example.buyingserver.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeAndMessage {

    // 공통 성공 응답
    SUCCESS_OK(200, "요청이 성공적으로 처리되었습니다."),
    SUCCESS_CREATED(201, "리소스가 성공적으로 생성되었습니다."),
    SUCCESS_NO_CONTENT(204, "요청이 성공했지만 반환할 내용이 없습니다."),

    // 회원 관련
    MEMBER_CREATED(201, "회원이 성공적으로 생성되었습니다."),
    MEMBER_FOUND(200, "회원 정보 조회에 성공했습니다."),
    MEMBER_NOT_FOUND(404, "해당 회원을 찾을 수 없습니다."),

    // 인증 관련
    AUTH_SUCCESS(200, "로그인에 성공했습니다."),
    AUTH_FAILED(401, "인증에 실패했습니다."),
    TOKEN_EXPIRED(401, "JWT 토큰이 만료되었습니다."),
    TOKEN_INVALID(401, "JWT 토큰이 유효하지 않습니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.");

    private final int code;
    private final String message;
}
