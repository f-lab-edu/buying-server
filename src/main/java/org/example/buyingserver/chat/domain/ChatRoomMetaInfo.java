package org.example.buyingserver.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
@Document(collection = "chat_room_meta")
public class ChatRoomMetaInfo {

    @Id
    private Long roomId;

    private String lastMessage;
    private Instant lastMessageTime;

    private Map<Long, ParticipantMeta> participants = new ConcurrentHashMap<>();

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

    //일대일 채팅방 용
    public void enterRoom(Long memberId, boolean connected) {
        ParticipantMeta meta = participants.get(memberId);
        if (meta != null) {
            //모든 미읽은 초기화히기
            meta.readAll();
            //연결상태 업데이트
            if (connected) meta.connect();
            else meta.disconnect();
        }
    }

    public void addParticipant(Long memberId) {
        participants.putIfAbsent(memberId, new ParticipantMeta(memberId));
    }
}