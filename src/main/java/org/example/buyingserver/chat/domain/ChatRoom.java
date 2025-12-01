package org.example.buyingserver.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_room", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_chat_room_post_attend_user",
                columnNames = {"post_id", "attend_user_id"}
        )
}
)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType roomType;

    // 게시물
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 판매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private Member createUser;

    //구매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attend_user_id")
    private Member attendUser;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private LocalDateTime exitedAt;

    @Builder
    private ChatRoom(ChatRoomType roomType,
                     Post post,
                     Member createUser,
                     Member attendUser,
                     LocalDateTime createdAt,
                     LocalDateTime deletedAt,
                     LocalDateTime exitedAt) {

        this.roomType = roomType;
        this.post = post;
        this.createUser = createUser;
        this.attendUser = attendUser;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.deletedAt = deletedAt;
        this.exitedAt = exitedAt;
    }

    //프라이빗 채팅방 생성
    public static ChatRoom createPrivateRoom(Post post, Member createUser, Member attendUser) {
        return ChatRoom.builder()
                .roomType(ChatRoomType.PRIVATE)
                .post(post)
                .createUser(createUser)
                .attendUser(attendUser)
                .build();
    }

    // 오픈채팅방 생성
    public static ChatRoom createOpenRoom(Post post, Member createUser) {
        return ChatRoom.builder()
                .roomType(ChatRoomType.OPEN)
                .post(post)
                .createUser(createUser)
                .build();
    }

    // 방 삭제 처리
    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    // 방 퇴장 처리
    public void markExited() {
        this.exitedAt = LocalDateTime.now();
    }
}