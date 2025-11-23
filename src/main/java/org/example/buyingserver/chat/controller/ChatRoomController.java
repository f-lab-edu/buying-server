package org.example.buyingserver.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.dto.*;
import org.example.buyingserver.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<ChatMessagesResponse> getMessages(Long roomId) {
        return ResponseEntity.ok(chatRoomService.getMessages(roomId));
    }
}