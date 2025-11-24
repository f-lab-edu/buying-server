package org.example.buyingserver.chat.repository;


import org.example.buyingserver.chat.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
    ChatMessage findTopByRoomIdOrderByCreatedAtDesc(Long roomId);
    //unread개수찾는 쿼리
    long countByRoomIdAndReadByNotContaining(Long roomId, Long memberId);

}
