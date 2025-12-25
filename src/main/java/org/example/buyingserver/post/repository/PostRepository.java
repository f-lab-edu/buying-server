package org.example.buyingserver.post.repository;

import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.example.buyingserver.post.domain.Post;
import org.example.buyingserver.post.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndStatusNot(Long id, PostStatus status);
    List<Post> findAllByStatusIn(List<PostStatus> statuses);
    boolean existsByIdAndStatusNot(Long id, PostStatus status);
    Optional<Post> findById(Long id);

    //비관적 락으로 Post 조회하도록 설정
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Post p where p.id= :id")
    Optional<Post> findByIdWithLock(@Param("id") Long id);
}