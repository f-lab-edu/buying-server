package org.example.buyingserver.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.domain.ChatRoomMetaInfo;
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
                .orElseGet(() -> {
                    ChatRoomMetaInfo newMeta = new ChatRoomMetaInfo(roomId);
                    metaRepository.save(newMeta);
                    return newMeta;
                });

        meta.updateLastMessage(writerId, event.content());
        metaRepository.save(meta);

        // SSE 알림 전송 (읽어야 하는 사람에게만)
        meta.getParticipants().values().forEach(participant -> {
            Long memberId = participant.getMemberId();
            if (!memberId.equals(writerId)) {
                sseEmitterService.sendMessageNotification(memberId, roomId);
            }
        });

        log.info("[MessageSavedEvent] roomId={} writerId={} message='{}'",
                roomId, writerId, event.content());
    }

    //채팅방 입장 이벤트 처리
    @Async
    @EventListener
    public void handleEnterRoomEvent(EnterRoomEvent event) {

        Long roomId = event.roomId();
        Long memberId = event.memberId();

        ChatRoomMetaInfo meta = metaRepository.findById(roomId)
                .orElseGet(() -> {
                    ChatRoomMetaInfo newMeta = new ChatRoomMetaInfo(roomId);
                    metaRepository.save(newMeta);
                    return newMeta;
                });

        meta.enterRoom(memberId,true);
        metaRepository.save(meta);

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