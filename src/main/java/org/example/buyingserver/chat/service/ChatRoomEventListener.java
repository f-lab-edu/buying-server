package org.example.buyingserver.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.domain.ChatRoomMetaInfo;
import org.example.buyingserver.chat.event.EnterRoomEvent;
import org.example.buyingserver.chat.repository.ChatRoomMetaRepository;

import org.example.buyingserver.chat.event.RoomCreatedEvent;
import org.example.buyingserver.chat.event.MessageSavedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final ChatRoomMetaRepository metaRepository;

    @Async
    @EventListener
    public void handleRoomCreatedEvent(RoomCreatedEvent event) {
        ChatRoomMetaInfo meta = new ChatRoomMetaInfo(event.roomId());
        meta.addParticipant(event.sellerId());
        meta.addParticipant(event.buyerId());
        metaRepository.save(meta);
    }

    @Async
    @EventListener
    //메세지 저장
    public void handleMessageSavedEvent(MessageSavedEvent event) {

        ChatRoomMetaInfo meta = metaRepository.findById(event.roomId())
                .orElse(new ChatRoomMetaInfo(event.roomId()));

        meta.updateLastMessage(event.writerId(), event.content());
        metaRepository.save(meta);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    //채팅방 입장 알람
    public void handleEnterRoomEvent(EnterRoomEvent event) {

        ChatRoomMetaInfo meta = metaRepository.findById(event.roomId())
                .orElseThrow();

        meta.enterRoom(event.memberId());
        metaRepository.save(meta);
    }
}