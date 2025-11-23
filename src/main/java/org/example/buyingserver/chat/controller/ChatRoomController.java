package org.example.buyingserver.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.dto.ChatMessageRequest;
import org.example.buyingserver.chat.dto.ChatRoomRequest;
import org.example.buyingserver.chat.dto.ChatRoomResponse;
import org.example.buyingserver.chat.service.ChatMessageService;
import org.example.buyingserver.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponse> getOrCreateRoom(@RequestBody ChatRoomRequest request) {
        Long roomId = chatRoomService.getOrCreateRoom(request);
        return ResponseEntity.ok(new ChatRoomResponse(roomId));
    }

    @GetMapping("/room/")


}