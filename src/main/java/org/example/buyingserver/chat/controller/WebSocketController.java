package org.example.buyingserver.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public String sendMessage(@DestinationVariable Long roomId, @Payload String message) {

        System.out.println("메세제내용 확인" + message);
        return message;
    }
}