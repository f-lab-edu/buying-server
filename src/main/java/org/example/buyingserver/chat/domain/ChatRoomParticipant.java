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
@Table(name = "chat_room_participant")
public class ChatRoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 참여한 채팅방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime exitedAt;

    @Builder
    private ChatRoomParticipant(ChatRoom chatRoom,
                                Member member,
                                LocalDateTime joinedAt,
                                LocalDateTime exitedAt) {

        this.chatRoom = chatRoom;
        this.member = member;
        this.joinedAt = joinedAt != null ? joinedAt : LocalDateTime.now();
        this.exitedAt = exitedAt;
    }

    public static ChatRoomParticipant join(ChatRoom room, Member member) {
        return ChatRoomParticipant.builder()
                .chatRoom(room)
                .member(member)
                .joinedAt(LocalDateTime.now())
                .build();
    }

}