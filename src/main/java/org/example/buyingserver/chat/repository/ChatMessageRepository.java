package org.example.buyingserver.chat.repository;


import org.example.buyingserver.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
    ChatMessage findTopByRoomIdOrderByCreatedAtDesc(Long roomId);

//    //ToDo: 커스텀으로 나눠
//    @Modifying
//    @Query("{ 'roomId': ?0 }")
//    @Update("{ '$addToSet': { 'readBy': ?1 } }")
//    void addReadByMemberId(Long roomId, Long memberId);
}
