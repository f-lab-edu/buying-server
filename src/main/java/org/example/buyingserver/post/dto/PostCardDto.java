package org.example.buyingserver.post.dto;

import org.example.buyingserver.member.dto.MemberCardDto;
import org.example.buyingserver.post.domain.Post;

public record PostCardDto(
        Long id,
        MemberCardDto member,
        String title,
        String thumbnailUrl,
        Integer price
) {
    public static PostCardDto fromEntity(Post post) {
        return new PostCardDto(
                post.getId(),
                MemberCardDto.from(post.getMember()),
                post.getTitle(),
                post.getThumbnailUrl(),
                post.getPrice()
        );
    }
}