package org.example.buyingserver.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.dto.ChatMessageRequest;
import org.example.buyingserver.chat.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest request
    ) {
        ChatMessage saved = chatMessageService.save(roomId, request);
        // 브로드캐스트 처리
        messagingTemplate.convertAndSend("/topic/" + roomId, saved);
    }
}