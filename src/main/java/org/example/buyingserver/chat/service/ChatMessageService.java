package org.example.buyingserver.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.dto.ChatMessageRequest;
import org.example.buyingserver.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(Long roomId, ChatMessageRequest request) {

        ChatMessage message = ChatMessage.createText(
                roomId,
                request.senderId(),
                request.content()
        );

        return chatMessageRepository.save(message);
    }
}