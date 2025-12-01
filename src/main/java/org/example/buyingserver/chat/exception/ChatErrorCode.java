package org.example.buyingserver.chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND(404, 5001, "채팅방을 찾을 수 없습니다."),
    PARTICIPANT_NOT_FOUND(404, 5002, "채팅방 참여자가 존재하지 않습니다."),
    CHAT_ACCESS_DENIED(403, 5003, "채팅방에 접근 권한이 없습니다."),
    INVALID_CHAT_REQUEST(400, 5004, "잘못된 채팅 요청입니다."),
    BUYER_CANNOT_BE_SELLER(400, 7003, "판매자는 자신의 게시물에 채팅을 시작할 수 없습니다."),
    CHATROOM_ALREADY_EXISTS(400, 7001, "이미 채팅방이 존재합니다.");
    private final int status;
    private final int code;
    private final String message;
}