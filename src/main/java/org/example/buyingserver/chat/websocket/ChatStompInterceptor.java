package org.example.buyingserver.chat.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.event.EnterRoomEvent;
import org.example.buyingserver.chat.event.WebSocketDisconnectEvent;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.member.exception.MemberNotFoundException;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatStompInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (command == null) {
            return message;
        }

        // 방 입장 + SESSION 저장
        if (command == StompCommand.SUBSCRIBE) {

            String destination = accessor.getDestination(); // 예) /topic/1
            Long roomId = extractRoomId(destination);

            // 프론트에서 Authorization 헤더
            String token = accessor.getFirstNativeHeader("Authorization");
            String email = jwtTokenProvider.getEmailFromToken(token);

            Long memberId = memberRepository.findByEmail(email)
                    .orElseThrow(MemberNotFoundException::new)
                    .getId();

            log.info("STOMP SUBSCRIBE : member={} room={}", memberId, roomId);

            eventPublisher.publishEvent(new EnterRoomEvent(roomId, memberId));

            accessor.getSessionAttributes().put("roomId", roomId);
            accessor.getSessionAttributes().put("memberId", memberId);
        }

        // 웹소켓 연결 종료
        if (command == StompCommand.DISCONNECT) {

            Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
            Long memberId = (Long) accessor.getSessionAttributes().get("memberId");

            if (roomId != null && memberId != null) {

                log.info("STOMP DISCONNECT : member={} room={}", memberId, roomId);

                // 연결 종료 이벤트 발행
                eventPublisher.publishEvent(
                        new WebSocketDisconnectEvent(roomId, memberId)
                );
            } else {
                log.warn("DISCONNECT 발생했으나 roomId/memberId 없음 → 세션 만료 가능");
            }
        }

        return message;
    }

    private Long extractRoomId(String dest) {
        return Long.valueOf(dest.substring(dest.lastIndexOf("/") + 1));
    }
}
