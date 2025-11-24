package org.example.buyingserver.chat.repository;

import org.example.buyingserver.chat.domain.ChatRoomMetaInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomMetaRepository extends MongoRepository<ChatRoomMetaInfo, Long> {
}