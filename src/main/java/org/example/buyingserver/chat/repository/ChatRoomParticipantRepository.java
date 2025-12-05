package org.example.buyingserver.chat.repository;

import org.example.buyingserver.chat.domain.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
    List<ChatRoomParticipant> findByMemberId(Long memberId);
    List<ChatRoomParticipant> findByChatRoom_Id(Long roomId);
}
