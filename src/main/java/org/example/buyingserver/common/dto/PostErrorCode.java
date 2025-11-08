package org.example.buyingserver.common.dto;

import lombok.Getter;
import org.example.buyingserver.common.dto.ResponseCodeAndMessage;

@Getter
public enum PostErrorCode implements ResponseCodeAndMessage {

    POST_NOT_FOUND(404, "게시물을 찾을 수 없습니다."),
    POST_ALREADY_DELETED(400, "이미 삭제된 게시물입니다."),
    POST_ALREADY_RESERVED(400, "이미 예약된 게시물입니다."),
    POST_CANNOT_RESERVE_DELETED(400, "삭제된 게시물은 예약할 수 없습니다.");

    private final int code;
    private final String message;

    PostErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}