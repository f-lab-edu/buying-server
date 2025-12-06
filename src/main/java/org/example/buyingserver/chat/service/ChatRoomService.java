package org.example.buyingserver.chat.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.chat.domain.ChatMessage;
import org.example.buyingserver.chat.domain.ChatRoom;
import org.example.buyingserver.chat.domain.ChatRoomParticipant;
import org.example.buyingserver.chat.domain.ChatRoomMetaInfo;
import org.example.buyingserver.chat.domain.ParticipantMeta;
import org.example.buyingserver.chat.dto.*;
import org.example.buyingserver.chat.event.EnterRoomEvent;
import org.example.buyingserver.chat.event.MessageSavedEvent;
import org.example.buyingserver.chat.event.RoomCreatedEvent;
import org.example.buyingserver.chat.exception.BuyerConflictWithSellerException;
import org.example.buyingserver.chat.exception.ChatRoomAlreadyExistsException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        // ToDo: Valid 추가해야함
        public ChatMessage save(Long roomId, ChatMessageRequest request) {

                if (request.content() == null || request.content().trim().isEmpty()) {
                        throw new IllegalArgumentException("메시지 내용은 필수입니다.");
                }

                ChatMessage message = ChatMessage.createText(
                                roomId,
                                request.writerId(),
                                request.content());

                ChatMessage saved = chatMessageRepository.save(message);

                List<Long> participantIds = findParticipant(roomId);

                eventPublisher.publishEvent(
                                new MessageSavedEvent(
                                                saved.getRoomId(),
                                                saved.getWriterId(),
                                                saved.getId(),
                                                saved.getContent(),
                                                participantIds));

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
                                        try {
                                                Long newRoomId = createNewRoom(post, seller, buyer);

                                                // 신규 방일 때만 이벤트 발행
                                                eventPublisher.publishEvent(
                                                                new RoomCreatedEvent(buyerId, sellerId, newRoomId));

                                                return newRoomId;
                                        } catch (DataIntegrityViolationException e) {
                                                return chatRoomRepository.findByPostIdAndAttendUserId(postId, buyerId)
                                                                .orElseThrow(() -> new ChatRoomAlreadyExistsException())
                                                                .getId();
                                        }
                                });
        }

        /**
         * 채팅방 메시지 조회 + 읽음 처리 이벤트
         */
        public ChatMessagesResponse getMessages(Long roomId, Long memberId) {

                eventPublisher.publishEvent(new EnterRoomEvent(roomId, memberId));

                List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);

                List<ChatMessageResponse> dtoList = messages.stream()
                                .map(ChatMessageResponse::from)
                                .toList();

                return ChatMessagesResponse.from(dtoList);
        }

        /**
         * 내가 속한 채팅방 리스트 조회
         */
        public ChatRoomListResponse getMyChatRooms(Long memberId) {
                // 참여자 목록 조회
                List<ChatRoomParticipant> participants = participantRepository.findByMemberId(memberId);

                if (participants.isEmpty()) {
                        return ChatRoomListResponse.from(List.of());
                }

                //  roomId 리스트 수집
                List<Long> roomIds = participants.stream()
                                .map(p -> p.getChatRoom().getId())
                                .toList();

                //모든 메타 정보 한 번에 조회
                Map<Long, ChatRoomMetaInfo> metaMap = metaRepository.findAllById(roomIds)
                                .stream()
                                .collect(Collectors.toMap(
                                                ChatRoomMetaInfo::getRoomId,
                                                Function.identity()));

                // 모든 참여자 한 번에 조회
                Map<Long, List<ChatRoomParticipant>> participantsByRoom = participantRepository
                                .findByChatRoom_IdIn(roomIds)
                                .stream()
                                .collect(Collectors.groupingBy(p -> p.getChatRoom().getId()));

                //마지막 메시지 ID 수집 및 조회
                Set<String> lastMessageIds = metaMap.values().stream()
                                .map(ChatRoomMetaInfo::getLastMessageId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());

                Map<String, ChatMessage> lastMessageMap = lastMessageIds.isEmpty()
                                ? Map.of()
                                : chatMessageRepository.findAllById(lastMessageIds)
                                                .stream()
                                                .collect(Collectors.toMap(
                                                                ChatMessage::getId,
                                                                Function.identity()));

                // DTO 변환
                List<ChatRoomListItemResponse> list = participants.stream()
                                .map(participant -> toChatRoomListItem(
                                                participant,
                                                memberId,
                                                metaMap,
                                                participantsByRoom,
                                                lastMessageMap))
                                .toList();

                return ChatRoomListResponse.from(list);
        }

        private ChatRoomListItemResponse toChatRoomListItem(
                        ChatRoomParticipant participant,
                        Long memberId,
                        Map<Long, ChatRoomMetaInfo> metaMap,
                        Map<Long, List<ChatRoomParticipant>> participantsByRoom,
                        Map<String, ChatMessage> lastMessageMap) {

                ChatRoom room = participant.getChatRoom();
                Long roomId = room.getId();

                // 상대방 찾기
                OpponentInfo opponent = findOpponent(roomId, memberId, participantsByRoom);

                // 메타 정보 및 마지막 메시지
                MessageInfo messageInfo = extractMessageInfo(roomId, memberId, metaMap, lastMessageMap);

                return new ChatRoomListItemResponse(
                                roomId,
                                room.getPost().getId(),
                                opponent.id(),
                                opponent.name(),
                                messageInfo.content(),
                                messageInfo.time(),
                                messageInfo.unreadCount());
        }

        /**
         * 상대방 정보 찾기
         */
        private OpponentInfo findOpponent(
                        Long roomId,
                        Long memberId,
                        Map<Long, List<ChatRoomParticipant>> participantsByRoom) {

                List<ChatRoomParticipant> roomParticipants = participantsByRoom
                                .getOrDefault(roomId, List.of());

                ChatRoomParticipant opponent = roomParticipants.stream()
                                .filter(p -> !p.getMember().getId().equals(memberId))
                                .findFirst()
                                .orElse(null);

                if (opponent == null) {
                        return new OpponentInfo(null, "알수없음");
                }

                return new OpponentInfo(
                                opponent.getMember().getId(),
                                opponent.getMember().getNickname());
        }

        /**
         * 메시지 정보 추출
         */
        private MessageInfo extractMessageInfo(
                        Long roomId,
                        Long memberId,
                        Map<Long, ChatRoomMetaInfo> metaMap,
                        Map<String, ChatMessage> lastMessageMap) {

                ChatRoomMetaInfo meta = metaMap.get(roomId);

                if (meta == null) {
                        return new MessageInfo("", "", 0);
                }

                // 마지막 메시지 정보
                String lastMessageId = meta.getLastMessageId();
                ChatMessage lastMessage = lastMessageId != null
                                ? lastMessageMap.get(lastMessageId)
                                : null;

                String content = lastMessage != null ? lastMessage.getContent() : "";
                String time = lastMessage != null ? lastMessage.getCreatedAt().toString() : "";

                // 미읽음 개수
                int unread = meta.getParticipants().values().stream()
                                .filter(pm -> pm.getMemberId().equals(memberId))
                                .findFirst()
                                .map(ParticipantMeta::getUnreadCount)
                                .orElse(0);

                return new MessageInfo(content, time, unread);
        }

        /**
         * 새로운 채팅방 생성
         */
        private Long createNewRoom(Post post, Member seller, Member buyer) {

                ChatRoom room = ChatRoom.createPrivateRoom(post, seller, buyer);
                chatRoomRepository.save(room);

                participantRepository.save(ChatRoomParticipant.join(room, seller));
                participantRepository.save(ChatRoomParticipant.join(room, buyer));

                return room.getId();
        }

        /**
         * 채팅방아이디 기반으로 전체 참여자 목록 조회
         */
        private List<Long> findParticipant(Long roomId) {
                List<Long> participantIds = participantRepository
                                .findByChatRoom_Id(roomId)
                                .stream()
                                .map(p -> p.getMember().getId())
                                .toList();
                return participantIds;
        }

}