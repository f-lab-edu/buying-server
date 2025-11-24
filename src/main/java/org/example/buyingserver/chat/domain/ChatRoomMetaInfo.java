package org.example.buyingserver.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@Document(collection = "chat_room_meta")
public class ChatRoomMetaInfo {

    @Id
    private Long roomId;

    private String lastMessage;
    private Instant lastMessageTime;

    private Map<Long, ParticipantMeta> participants = new HashMap<>();

    public ChatRoomMetaInfo(Long roomId) {
        this.roomId = roomId;
    }

    public void updateLastMessage(Long writerId, String message) {
        this.lastMessage = message;
        this.lastMessageTime = Instant.now();

        // 상대방 unreadCount +1 증가
        for (Map.Entry<Long, ParticipantMeta> entry : participants.entrySet()) {
            Long memberId = entry.getKey();
            ParticipantMeta meta = entry.getValue();

            if (!memberId.equals(writerId)) {
                meta.addUnread();
            }
        }
    }

    public void enterRoom(Long memberId) {
        ParticipantMeta meta = participants.get(memberId);
        if (meta != null) {
            meta.readAll();
        }
    }

    public void addParticipant(Long memberId) {
        participants.putIfAbsent(memberId, new ParticipantMeta(memberId));
    }
}