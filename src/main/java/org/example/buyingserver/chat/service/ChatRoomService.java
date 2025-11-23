package org.example.buyingserver.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.domain.ChatRoom;
import org.example.buyingserver.chat.domain.ChatRoomParticipant;
import org.example.buyingserver.chat.dto.ChatMessageRequest;
import org.example.buyingserver.chat.dto.ChatRoomRequest;
import org.example.buyingserver.chat.exception.BuyerConflictWithSellerException;
import org.example.buyingserver.chat.exception.InvalidChatRequestException;
import org.example.buyingserver.chat.repository.ChatMessageRepository;
import org.example.buyingserver.chat.repository.ChatRoomParticipantRepository;
import org.example.buyingserver.chat.repository.ChatRoomRepository;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.exception.MemberNotFoundException;
import org.example.buyingserver.member.repository.MemberRepository;
import org.example.buyingserver.post.domain.Post;
import org.example.buyingserver.post.exception.PostNotFoundException;
import org.example.buyingserver.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(Long roomId, ChatMessageRequest request) {

        ChatMessage message = ChatMessage.createText(
                roomId,
                request.senderId(),
                request.content()
        );

        return chatMessageRepository.save(message);
    }

    // buyer = 채팅방을 요청하는 사용자(구매자)
    @Transactional
    public Long getOrCreateRoom(ChatRoomRequest request) {

        if (request.postId() == null || request.buyerId() == null) {
            throw new InvalidChatRequestException();
        }

        Long postId = request.postId();
        Long buyerId = request.buyerId();

        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(MemberNotFoundException::new);

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Member seller = post.getMember();

        if (Objects.equals(seller.getId(), buyer.getId())) {
            throw new BuyerConflictWithSellerException();
        }

        // 기존 방 존재하면 그 방 반환, 없으면 생성
        return chatRoomRepository.findByPostIdAndAttendUserId(postId, buyerId)
                .map(ChatRoom::getId)
                .orElseGet(() -> createNewRoom(post, seller, buyer));
    }

    private Long createNewRoom(Post post, Member seller, Member buyer) {

        ChatRoom room = ChatRoom.createPrivateRoom(post, seller, buyer);
        chatRoomRepository.save(room);

        participantRepository.save(ChatRoomParticipant.join(room, post, seller));
        participantRepository.save(ChatRoomParticipant.join(room, post, buyer));

        return room.getId();
    }


}