package org.example.buyingserver.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.domain.ChatRoomMetaInfo;
import org.example.buyingserver.chat.domain.ParticipantMeta;
import org.example.buyingserver.chat.event.EnterRoomEvent;
import org.example.buyingserver.chat.event.WebSocketDisconnectEvent;
import org.example.buyingserver.chat.repository.ChatMessageRepository;
import org.example.buyingserver.chat.repository.ChatRoomMetaRepository;
import org.example.buyingserver.chat.event.RoomCreatedEvent;
import org.example.buyingserver.chat.event.MessageSavedEvent;
import org.example.buyingserver.chat.sse.ChatSseEmitterService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final ChatRoomMetaRepository metaRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSseEmitterService sseEmitterService;

    // 채팅방 생성 (1:1 기준)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoomCreatedEvent(RoomCreatedEvent event) {
        ChatRoomMetaInfo meta = new ChatRoomMetaInfo(event.roomId());
        meta.addParticipant(event.sellerId());
        meta.addParticipant(event.buyerId());
        metaRepository.save(meta);

        log.info("[RoomCreatedEvent] roomId={} seller={} buyer={}",
                event.roomId(), event.sellerId(), event.buyerId());
    }


    @Async
    @EventListener
    public void handleMessageSavedEvent(MessageSavedEvent event) {

        Long roomId = event.roomId();
        Long writerId = event.writerId();

        ChatRoomMetaInfo meta = metaRepository.findById(roomId)
                .orElseGet(() -> new ChatRoomMetaInfo(roomId));

        if (meta.getParticipants().isEmpty()) {
            event.participantIds().forEach(meta::addParticipant);
        }
        //메타데이터 컬랙션에 아이디값만 저장하도록 수정

        meta.updateLastMessage(writerId, event.content());

        meta.getParticipants().values().forEach(pm -> {
            if (!pm.getMemberId().equals(writerId) && pm.isConnected()) {
                chatMessageRepository.addReadByMemberId(roomId, pm.getMemberId());
                pm.readAll(); // unread = 0
            }
        });

        metaRepository.save(meta);

        //SSE 알림 (발신자 제외)
        meta.getParticipants().values().forEach(pm -> {
            if (!pm.getMemberId().equals(writerId)) {
                sseEmitterService.sendMessageNotification(pm.getMemberId(), roomId);
            }
        });

        log.info("[MessageSavedEvent] roomId={} writerId={} message='{}'",
                roomId, writerId, event.content());
    }


    @Async
    @EventListener
    public void handleEnterRoomEvent(EnterRoomEvent event) {

        Long roomId = event.roomId();
        Long memberId = event.memberId();

        ChatRoomMetaInfo meta = metaRepository.findById(roomId)
                .orElseGet(() -> new ChatRoomMetaInfo(roomId));

        meta.enterRoom(memberId, true);
        metaRepository.save(meta);

        // 몽고 메시지 readBy에 추가
        chatMessageRepository.addReadByMemberId(roomId, memberId);

        log.info("[EnterRoomEvent] memberId={} entered roomId={} → unread reset",
                memberId, roomId);
    }


    @Async
    @EventListener
    public void handleWebSocketDisconnectEvent(WebSocketDisconnectEvent event) {

        ChatRoomMetaInfo meta = metaRepository.findById(event.roomId())
                .orElse(null);

        if (meta == null) return;

        meta.enterRoom(event.memberId(), false);
        metaRepository.save(meta);

        log.info("[Disconnect] member {} disconnected room {}",
                event.memberId(), event.roomId());
    }

}