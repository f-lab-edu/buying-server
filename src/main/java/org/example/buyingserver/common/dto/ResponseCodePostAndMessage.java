package org.example.buyingserver.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCodePostAndMessage {
    //게시물 성공 관련
    SUCCESS_POST_CREATED(200, "게시물이 성공적으로 생성되었습니다."),
    SUCCESS_POST_FETCHED(200, "게시물 목록들을 성공적으로 가져왔습니다.");

    private final int code;
    private final String message;
}