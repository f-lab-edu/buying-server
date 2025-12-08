package org.example.buyingserver.chat.sse;


import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.dto.NewMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChatSseEmitterService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final Long TIMEOUT = 30 * 60 * 1000L;

    public SseEmitter createEmitter(Long memberId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        emitters.put(memberId, emitter);
        log.info("sse 연결 : : memberId={}", memberId);

        emitter.onCompletion(() -> emitters.remove(memberId));

        emitter.onTimeout(() -> {
            emitters.remove(memberId);
            log.info("연결 타이아웃 발생 제거");
        });

        emitter.onError((e) -> {
            emitters.remove(memberId);
            log.error("SSE 연결 에러: memberId={}", memberId, e);
        });
        return emitter;


    }

    public void sendMessageNotification(Long memberId, Long roomId) {
        SseEmitter emitter = emitters.get(memberId);
        log.warn("SSE emituer 에 들어온 맴버 아이디 {}", memberId);

        if (emitter == null) {
            log.warn("SSE 알람에서 맴버아이디를 못참음 {}", memberId);
            return;
        }

        try {
            log.info("[SSE-SEND- 보내기] memberId={}, roomId={}", memberId, roomId);
            emitter.send(SseEmitter.event()
                    .name("new-message")
                    .data(new NewMessage(roomId)));
            log.info("[SSE-SEND-SUCCESS] memberId={}, roomId={}", memberId, roomId);

        } catch (Exception e) {
            emitters.remove(memberId);
            log.error("SSE 전송 실패: memberId={} roomId={}", memberId, roomId, e);
        }

    }

}