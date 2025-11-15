package org.example.buyingserver.member.exception;

import org.example.buyingserver.common.dto.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND(404, 4001, "회원 정보를 찾을 수 없습니다."),
    INVALID_PASSWORD(400, 4002, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(400, 4003, "이미 가입된 이메일입니다."),
    MISSING_AUTHORIZATION_HEADER(400, 4004, "Authorization 헤더가 누락되었습니다.");

    private final int status;
    private final int code;
    private final String message;
}