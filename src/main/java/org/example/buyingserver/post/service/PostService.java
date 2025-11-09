package org.example.buyingserver.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.post.domain.Post;
import org.example.buyingserver.post.domain.PostDetail;
import org.example.buyingserver.post.domain.PostImage;
import org.example.buyingserver.post.dto.PostCreateRequestDto;
import org.example.buyingserver.post.dto.PostCreateResponseDto;
import org.example.buyingserver.post.repository.PostDetailRepository;
import org.example.buyingserver.post.repository.PostImageRepository;
import org.example.buyingserver.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto dto, Member member) {
        String thumbnailUrl = extractThumbnail(dto);
        Post post = Post.create(member, dto.title(), dto.price(), thumbnailUrl);
        postRepository.save(post);
        savePostDetailIfExists(dto, post);
        savePostImagesIfExists(dto, post);
        return new PostCreateResponseDto(post.getId());
    }

    // 썸네일 추출
    private String extractThumbnail(PostCreateRequestDto dto) {
        if (dto.images() == null || dto.images().isEmpty()) {
            return null;
        }
        return dto.images().get(0);
    }

    // 상세 내용 저장
    private void savePostDetailIfExists(PostCreateRequestDto dto, Post post) {
        if (dto.content() != null && !dto.content().isBlank()) {
            PostDetail detail = PostDetail.create(post, dto.content());
            postDetailRepository.save(detail);
        }
    }

    // 이미지 여러 장 저장
    private void savePostImagesIfExists(PostCreateRequestDto dto, Post post) {
        if (dto.images() != null && !dto.images().isEmpty()) {
            for (int i = 0; i < dto.images().size(); i++) {
                postImageRepository.save(PostImage.create(post, dto.images().get(i), i + 1));
            }
        }
    }
}