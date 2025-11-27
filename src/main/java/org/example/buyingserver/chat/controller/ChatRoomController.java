package org.example.buyingserver.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.dto.*;
import org.example.buyingserver.chat.service.ChatRoomService;
import org.example.buyingserver.chat.sse.ChatSseEmitterService;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.common.exception.ForbiddenException;
import org.example.buyingserver.common.exception.UnauthorizedException;
import org.example.buyingserver.member.exception.MemberNotFoundException;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatSseEmitterService chatSseEmitterService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponse> getOrCreateRoom(@RequestBody ChatRoomRequest request) {
        Long roomId = chatRoomService.getOrCreateRoom(request);
        return ResponseEntity.ok(new ChatRoomResponse(roomId));
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<ChatMessagesResponse> getMessages(@PathVariable Long roomId, @RequestParam Long memberId) {
        // chatRoomService.enterRoom(roomId, memberId);
        return ResponseEntity.ok(chatRoomService.getMessages(roomId, memberId));
    }

    // 현재 내가 속해있는 채팅방 목록 가져오기
    @GetMapping("/rooms/{memberId}")
    public ResponseEntity<ChatRoomListResponse> getMyRooms(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(chatRoomService.getMyChatRooms(memberId));
    }

    // sse 채팅방 리스트 페이지에서 실시간 메세지 업데이트용
    // 새로운 메시지가 오면 즉시 push
    @GetMapping(value = "/subscribe/{memberId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long memberId,
            @RequestParam(value = "token", required = false) String token) {

        Long loginUserId = getUserId(token);

        if (!loginUserId.equals(memberId)) {
            throw new ForbiddenException();
        }

        return chatSseEmitterService.createEmitter(memberId);
    }

    private Long getUserId(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException();
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new)
                .getId();
    }
}