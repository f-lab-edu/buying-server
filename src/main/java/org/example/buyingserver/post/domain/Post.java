package org.example.buyingserver.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.buyingserver.common.dto.PostErrorCode;
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

    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.SELLING;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    private Post(Member member, String title, Integer price, String thumbnailUrl,
                 PostStatus status) {
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
                .build();
    }

    public void markAsReserved() {
        if (this.status == PostStatus.DELETED) {
            throw new IllegalStateException(PostErrorCode.POST_ALREADY_DELETED.getMessage());
        }
        this.status = PostStatus.RESERVED;
    }

    public void markAsDeleted() {
        if (this.status == PostStatus.DELETED) {
            return;
        }
        this.status = PostStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    public void cancelReservation() {
        if (this.status != PostStatus.RESERVED) {
            throw new IllegalStateException(PostErrorCode.POST_NOT_RESERVED.getMessage());
        }
        this.status = PostStatus.SELLING;
    }

    public void markAsSold() {
        if (this.status == PostStatus.DELETED) {
            throw new IllegalStateException(PostErrorCode.POST_ALREADY_DELETED.getMessage());
        }
        this.status = PostStatus.SOLD;
    }
}
