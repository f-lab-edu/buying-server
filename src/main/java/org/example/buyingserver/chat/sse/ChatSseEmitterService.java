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
    //쓰기성능을 위해ㅑ ConcurrentHashMap 사용
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final Long TIMEOUT = 30 * 60 * 1000L;

    public SseEmitter createEmitter(Long memberId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        emitters.put(memberId, emitter);
        log.info("sse 연결 : : memberId={}", memberId);

        // 연결 종료 시 제거
        emitter.onCompletion(() -> emitters.remove(memberId));

        //타임아웃시
        emitter.onTimeout(() -> {
            emitters.remove(memberId);
            log.info("연결 타이아웃 발생 제거");
        });

        //에러발생
        emitter.onError((e) -> {
            emitters.remove(memberId);
            log.error("SSE 연결 에러: memberId={}", memberId, e);
        });
        return emitter;


    }

    //사용자에게 속한 채팅방에서 새로운 메세지 알림 전송
    public void sendMessageNotification(Long memberId, Long roomId) {
        SseEmitter emitter = emitters.get(memberId);

        if (emitter == null) {
            log.warn("SSE emitter not found for member {}", memberId);
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("new-message")
                    .data(new NewMessage(roomId)));
        } catch (Exception e) {
            emitters.remove(memberId);
            log.error("SSE 전송 실패: memberId={} roomId={}", memberId, roomId, e);
        }

    }

}