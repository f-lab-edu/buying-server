package org.example.buyingserver.chat.dto;

import jakarta.validation.constraints.NotNull;

public record ChatRoomRequest(
        @NotNull(message = "게시글 ID는 필수 입력 항목입니다.")
        Long postId,
        @NotNull(message = "구매자 ID는 필수 입력 항목입니다.")
        Long buyerId
) {}