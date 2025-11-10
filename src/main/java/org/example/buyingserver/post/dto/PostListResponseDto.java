package org.example.buyingserver.post.dto;

import java.util.List;

public record PostListResponseDto(
        List<PostCardDto> posts
) {
    public static PostListResponseDto from(List<PostCardDto> postCards) {
        return new PostListResponseDto(postCards);
    }
}
