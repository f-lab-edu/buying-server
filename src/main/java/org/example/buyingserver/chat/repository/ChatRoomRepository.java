package org.example.buyingserver.chat.repository;

import org.example.buyingserver.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByPostIdAndAttendUserId(Long postId, Long attend_user_id);

}
