package org.example.buyingserver.chat.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.domain.ChatRoom;
import org.example.buyingserver.chat.domain.ChatRoomParticipant;
import org.example.buyingserver.chat.domain.ChatRoomMetaInfo;
import org.example.buyingserver.chat.dto.*;
import org.example.buyingserver.chat.event.EnterRoomEvent;
import org.example.buyingserver.chat.event.MessageSavedEvent;
import org.example.buyingserver.chat.event.RoomCreatedEvent;
import org.example.buyingserver.chat.exception.BuyerConflictWithSellerException;
import org.example.buyingserver.chat.repository.ChatMessageRepository;
import org.example.buyingserver.chat.repository.ChatRoomParticipantRepository;
import org.example.buyingserver.chat.repository.ChatRoomRepository;
import org.example.buyingserver.chat.repository.ChatRoomMetaRepository;
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
    private final ChatRoomMetaRepository metaRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 메시지 저장 + 이벤트 발행
     */
    //ToDo: Valid 추가해야함
    public ChatMessage save(Long roomId, ChatMessageRequest request) {

        if (request.content() == null || request.content().trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 필수입니다.");
        }

        ChatMessage message = ChatMessage.createText(
                roomId,
                request.writerId(),
                request.content()
        );

        ChatMessage saved = chatMessageRepository.save(message);

        eventPublisher.publishEvent(
                new MessageSavedEvent(
                        saved.getRoomId(),
                        saved.getWriterId(),
                        saved.getContent()
                )
        );

        return saved;
    }

    /**
     * 채팅방 조회 및 생성
     */
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

        if (Objects.equals(sellerId, buyerId)) {
            throw new BuyerConflictWithSellerException();
        }

        // 기존 방이 있는 경우
        return chatRoomRepository.findByPostIdAndAttendUserId(postId, buyerId)
                .map(ChatRoom::getId)
                .orElseGet(() -> {
                    Long newRoomId = createNewRoom(post, seller, buyer);

                    // 신규 방일 때만 이벤트 발행
                    eventPublisher.publishEvent(
                            new RoomCreatedEvent(buyerId, sellerId, newRoomId)
                    );

                    return newRoomId;
                });
    }

    /**
     * 채팅방 메시지 조회 + 읽음 처리 이벤트
     */
    public ChatMessagesResponse getMessages(Long roomId, Long memberId) {

        eventPublisher.publishEvent(new EnterRoomEvent(roomId, memberId));

        List<ChatMessage> messages =
                chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);

        List<ChatMessageResponse> dtoList = messages.stream()
                .map(ChatMessageResponse::from)
                .toList();

        return ChatMessagesResponse.from(dtoList);
    }

    /**
     * 내가 속한 채팅방 리스트 조회
     */
    //ToDo : 코드 분리해서 리팩터링해야함
    public ChatRoomListResponse getMyChatRooms(Long memberId) {

        List<ChatRoomParticipant> participants =
                participantRepository.findByMemberId(memberId);

        List<ChatRoomListItemResponse> list = participants.stream()
                .map(participant -> {

                    ChatRoom room = participant.getChatRoom();
                    Long roomId = room.getId();

                    // 상대방 찾기
                    ChatRoomParticipant opponent = participantRepository
                            .findByChatRoom_Id(roomId)
                            .stream()
                            .filter(p -> !p.getMember().getId().equals(memberId))
                            .findFirst()
                            .orElse(null);

                    Long opponentId = opponent != null ? opponent.getMember().getId() : null;
                    String opponentName = opponent != null ? opponent.getMember().getNickname() : "알수없음";

                    // 마지막 메시지 조회
                    ChatMessage lastMessage = chatMessageRepository
                            .findTopByRoomIdOrderByCreatedAtDesc(roomId);

                    String lastContent = lastMessage != null ? lastMessage.getContent() : "";
                    String lastTime = lastMessage != null ? lastMessage.getCreatedAt().toString() : "";

                    ChatRoomMetaInfo meta = metaRepository.findById(roomId).orElse(null);
                    int unread = 0;

                    if (meta != null && meta.getParticipants().containsKey(memberId)) {
                        unread = meta.getParticipants().get(memberId).getUnreadCount();
                    }

                    return new ChatRoomListItemResponse(
                            roomId,
                            room.getPost().getId(),
                            opponentId,
                            opponentName,
                            lastContent,
                            lastTime,
                            unread
                    );
                })
                .toList();

        return ChatRoomListResponse.from(list);
    }

//    /**
//     * 방 입장 이벤트 수동 호출
//     */
//    public void enterRoom(Long roomId, Long memberId) {
//        eventPublisher.publishEvent(new EnterRoomEvent(roomId, memberId));
//        //ToDo : 몽고디비 readBy id도 넣어줘야함
//    }

    /**
     * 새로운 채팅방 생성
     */
    private Long createNewRoom(Post post, Member seller, Member buyer) {

        ChatRoom room = ChatRoom.createPrivateRoom(post, seller, buyer);
        chatRoomRepository.save(room);

        participantRepository.save(ChatRoomParticipant.join(room, post, seller));
        participantRepository.save(ChatRoomParticipant.join(room, post, buyer));

        return room.getId();
    }
}
