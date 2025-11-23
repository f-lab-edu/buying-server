package org.example.buyingserver.chat.repository;

import org.example.buyingserver.chat.domain.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
}
