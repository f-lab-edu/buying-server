package org.example.buyingserver.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.buyingserver.member.domain.Member;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = true)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.SELLING;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Builder
    private Post(Member member, String title, Integer price, String thumbnailUrl,
                 PostStatus status, LocalDateTime createdAt) {
        this.member = member;
        this.title = title;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.status = (status != null) ? status : PostStatus.SELLING;
    }

    public static Post create(Member member, String title, Integer price, String thumbnailUrl) {
        return Post.builder()
                .member(member)
                .title(title)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .status(PostStatus.SELLING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void markAsReserved() {
        if (this.status == PostStatus.DELETED) {
            throw new IllegalStateException("삭제된 게시물은 예약할 수 없습니다.");
        }
        this.status = PostStatus.RESERVED;
    }

    public void markAsDeleted() {
        if (this.status == PostStatus.DELETED) {
            return; // 이미 삭제됨
        }
        this.status = PostStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }



}
