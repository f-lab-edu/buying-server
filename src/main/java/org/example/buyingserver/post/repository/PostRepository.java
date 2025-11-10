package org.example.buyingserver.post.repository;

import org.example.buyingserver.post.domain.Post;
import org.example.buyingserver.post.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndStatusNot(Long id, PostStatus status);
    List<Post> findAllByStatusIn(List<PostStatus> statuses);
    boolean existsByIdAndStatusNot(Long id, PostStatus status);
    Optional<Post> findById(Long id);
}