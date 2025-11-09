package org.example.buyingserver.post.repository;

import org.example.buyingserver.post.domain.PostDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostDetailRepository extends JpaRepository<PostDetail, Long> {

    // 설계상 Post 당 1개의 상세 본문을 갖는다고 가정
    Optional<PostDetail> findByPostId(Long postId);

    // 필요 시 삭제(혹은 대체)용
    void deleteByPostId(Long postId);
}
