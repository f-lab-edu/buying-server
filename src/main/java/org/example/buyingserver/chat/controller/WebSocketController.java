package org.example.buyingserver.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.dto.ChatMessageRequest;
import org.example.buyingserver.chat.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest request) {
            ChatMessage saved = chatRoomService.save(roomId, request);
            // 브로드캐스트 처리
            messagingTemplate.convertAndSend("/topic/" + roomId, saved);

    }
}