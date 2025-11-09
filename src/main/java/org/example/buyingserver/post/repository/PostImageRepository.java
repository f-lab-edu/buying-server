package org.example.buyingserver.post.repository;

import org.example.buyingserver.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    // 상세 화면용: 정렬된 이미지 목록
    List<PostImage> findByPostIdOrderByImageOrderAsc(Long postId);

    // 수정/삭제 시 일괄 정리용
    void deleteByPostId(Long postId);
}
