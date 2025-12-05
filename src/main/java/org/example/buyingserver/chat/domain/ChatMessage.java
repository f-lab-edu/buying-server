package org.example.buyingserver.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private Long roomId;        // MySQL ChatRoom PK
    private Long writerId;      // MySQL Member PK

    private String content;

    private MessageType messageType;

    private List<String> attachments = new ArrayList<>();

    private Instant createdAt;

    @Builder
    private ChatMessage(Long roomId,
                        Long writerId,
                        String content,
                        MessageType messageType,
                        List<String> attachments,
                        Instant createdAt) {

        this.roomId = roomId;
        this.writerId = writerId;
        this.content = content;
        this.messageType = messageType != null ? messageType : MessageType.TEXT;
        this.attachments = attachments != null ? attachments : new ArrayList<>();
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    // 텍스트 메시지
    public static ChatMessage createText(Long roomId, Long writerId, String content) {
        return ChatMessage.builder()
                .roomId(roomId)
                .writerId(writerId)
                .content(content)
                .messageType(MessageType.TEXT)
                .createdAt(Instant.now())
                .build();
    }

    // 이미지 메시지
    public static ChatMessage createImage(Long roomId, Long writerId, String content, List<String> attachments) {
        return ChatMessage.builder()
                .roomId(roomId)
                .writerId(writerId)
                .content(content)
                .attachments(attachments)
                .messageType(MessageType.IMAGE)
                .build();
    }

    public static ChatMessage createSystem(Long roomId, String content) {
        return ChatMessage.builder()
                .roomId(roomId)
                .writerId(null)
                .content(content)
                .messageType(MessageType.SYSTEM)
                .build();
    }
}