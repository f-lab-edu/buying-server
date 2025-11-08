package org.example.buyingserver.post.domain;

import jakarta.persistence.*;
        import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "post_detail")
public class PostDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(columnDefinition = "TEXT")
    private String content;


    @Builder
    private PostDetail(Post post, String content) {
        this.post = post;
        this.content = content;
    }

    public static PostDetail create(Post post, String content) {
        return PostDetail.builder()
                .post(post)
                .content(content)
                .build();
    }
}