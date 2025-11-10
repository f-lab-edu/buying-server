package org.example.buyingserver.post.dto;

import org.example.buyingserver.member.dto.MemberCardDto;
import org.example.buyingserver.post.domain.Post;
import org.example.buyingserver.post.domain.PostDetail;
import org.example.buyingserver.post.domain.PostImage;
import org.example.buyingserver.post.domain.PostStatus;

import java.util.List;

public record PostDetailResponseDto(
        Long id,
        MemberCardDto member,
        String title,
        String content,
        Integer price,
        PostStatus status,
        Integer quantity,
        List<String> images
) {
    public static PostDetailResponseDto from(Post post, PostDetail postDetail, List<PostImage> postImages) {
        List<String> imageUrls = postImages.stream()
                .map(PostImage::getImageUrl)
                .toList();

        return new PostDetailResponseDto(
                post.getId(),
                MemberCardDto.from(post.getMember()),
                post.getTitle(),
                postDetail != null ? postDetail.getContent() : null,
                post.getPrice(),
                post.getStatus(),
                postDetail != null ? postDetail.getQuantity() : null,
                imageUrls
        );
    }
}