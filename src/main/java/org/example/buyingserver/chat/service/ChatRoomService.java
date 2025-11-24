package org.example.buyingserver.chat.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.domain.ChatRoom;
import org.example.buyingserver.chat.domain.ChatRoomParticipant;
import org.example.buyingserver.chat.dto.*;
import org.example.buyingserver.chat.event.RoomCreatedEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ChatMessage save(Long roomId, ChatMessageRequest request) {
        // 입력 검증
        if (request.content() == null || request.content().trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 필수입니다.");
        }
        ChatMessage message = ChatMessage.createText(
                roomId,
                request.writerId(),
                request.content());

        ChatMessage saved = chatMessageRepository.save(message);

        return saved;
    }

    // buyer = 채팅방을 요청하는 사용자(구매자)
    @Transactional
    public Long getOrCreateRoom(@Valid ChatRoomRequest request) {

        Long postId = request.postId();
        Long buyerId = request.buyerId();

        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(MemberNotFoundException::new);

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Member seller = post.getMember();
        Long sellerId = seller.getId();

        if (Objects.equals(seller.getId(), buyer.getId())) {
            throw new BuyerConflictWithSellerException();
        }

        Long roomId = chatRoomRepository.findByPostIdAndAttendUserId(postId, buyerId)
                .map(ChatRoom::getId)
                .orElseGet(() -> createNewRoom(post, seller, buyer));

        eventPublisher.publishEvent(
                new RoomCreatedEvent(
                        buyerId,
                        sellerId,
                        roomId
                        )
        );

        // 기존 방 존재하면 그 방 반환, 없으면 생성
        return roomId;
    }

    public ChatMessagesResponse getMessages(Long roomId) {

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);

        List<ChatMessageResponse> dtoList = messages.stream()
                .map(ChatMessageResponse::from)
                .toList();
        return ChatMessagesResponse.from(dtoList);
    }

    public ChatRoomListResponse getMyChatRooms(Long memberId) {

        //내가 속한 모든 방 조회
        List<ChatRoomParticipant> participants =
                participantRepository.findByMemberId(memberId);

        List<ChatRoomListItemResponse> list = participants.stream()
                .map(participant -> {
                    ChatRoom room = participant.getChatRoom();
                    Long roomId = room.getId();

                    //상대방 찾기
                    ChatRoomParticipant opponent = participantRepository
                            .findByChatRoom_Id(room.getId())
                            .stream()
                            .filter(p -> !p.getMember().getId().equals(memberId))
                            .findFirst()
                            .orElse(null);

                    Long opponentId = opponent != null ? opponent.getMember().getId() : null;
                    String opponentName = opponent != null ? opponent.getMember().getNickname() : "알수없음";

                    //마지막 메시지 조회
                    ChatMessage lastMessage = chatMessageRepository
                            .findTopByRoomIdOrderByCreatedAtDesc(room.getId());

                    String lastContent = lastMessage != null ? lastMessage.getContent() : "";
                    String lastTime = lastMessage != null ? lastMessage.getCreatedAt().toString() : "";

                    long unread = chatMessageRepository.countByRoomIdAndReadByNotContaining(roomId, memberId);

                    return new ChatRoomListItemResponse(
                            room.getId(),
                            room.getPost().getId(),
                            opponentId,
                            opponentName,
                            lastContent,
                            lastTime,
                            (int)unread
                    );
                })
                .toList();

        return ChatRoomListResponse.from(list);
    }


    private Long createNewRoom(Post post, Member seller, Member buyer) {

        ChatRoom room = ChatRoom.createPrivateRoom(post, seller, buyer);
        chatRoomRepository.save(room);

        participantRepository.save(ChatRoomParticipant.join(room, post, seller));
        participantRepository.save(ChatRoomParticipant.join(room, post, buyer));

        return room.getId();
    }

}