package org.example.buyingserver.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(404, 4001, "게시물을 찾을 수 없습니다."),
    POST_NOT_RESERVED(400, 4002, "예약된 게시물이 아닙니다."),
    POST_ALREADY_DELETED(400, 4003, "이미 삭제된 게시물입니다."),
    POST_ALREADY_RESERVED(400, 4004, "이미 예약된 게시물입니다."),
    POST_DETAIL_NOT_FOUND(404, 4005, "게시글 상세내용을 찾을 수 없습니다."),
    POST_CANNOT_RESERVE_DELETED(400, 4006, "삭제된 게시물을 예약할 수 없습니다.");

    private final int status;   // HTTP 상태 코드
    private final int code;     // 비즈니스 코드
    private final String message;
}