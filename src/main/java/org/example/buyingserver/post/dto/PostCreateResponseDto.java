package org.example.buyingserver.post.dto;

public record PostCreateResponseDto(Long postId) {

    public static PostCreateResponseDto from(Long postId) {
        return new PostCreateResponseDto(postId);
    }
}