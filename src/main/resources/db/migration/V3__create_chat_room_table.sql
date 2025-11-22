-- =========================================
-- V3: ChatRoom & ChatRoomParticipant 테이블 생성
-- =========================================

-- 1. ChatRoom 테이블 생성
CREATE TABLE chat_room (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           room_type VARCHAR(20) NOT NULL,

                           post_id BIGINT NOT NULL,
                           create_user_id BIGINT,
                           attend_user_id BIGINT,

                           created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                           deleted_at DATETIME(6) NULL,
                           exited_at DATETIME(6) NULL,

                           CONSTRAINT fk_chatroom_post
                               FOREIGN KEY (post_id) REFERENCES post(id),

                           CONSTRAINT fk_chatroom_create_user
                               FOREIGN KEY (create_user_id) REFERENCES member(id),

                           CONSTRAINT fk_chatroom_attend_user
                               FOREIGN KEY (attend_user_id) REFERENCES member(id)
);

-- 인덱스 추가
CREATE INDEX idx_chat_room_post_id ON chat_room(post_id);
CREATE INDEX idx_chat_room_create_user_id ON chat_room(create_user_id);
CREATE INDEX idx_chat_room_attend_user_id ON chat_room(attend_user_id);


-- 2. ChatRoomParticipant 테이블 생성
CREATE TABLE chat_room_participant (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                       chat_room_id BIGINT NOT NULL,
                                       post_id BIGINT NOT NULL,
                                       member_id BIGINT NOT NULL,

                                       joined_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                                       exited_at DATETIME(6) NULL,

                                       CONSTRAINT fk_participant_chatroom
                                           FOREIGN KEY (chat_room_id) REFERENCES chat_room(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT fk_participant_post
                                           FOREIGN KEY (post_id) REFERENCES post(id),

                                       CONSTRAINT fk_participant_member
                                           FOREIGN KEY (member_id) REFERENCES member(id)
);

-- 인덱스 추가
CREATE INDEX idx_participant_chat_room_id ON chat_room_participant(chat_room_id);
CREATE INDEX idx_participant_post_id ON chat_room_participant(post_id);
CREATE INDEX idx_participant_member_id ON chat_room_participant(member_id);

-- 빠른 조회용 복합 인덱스
CREATE INDEX idx_participant_room_member ON chat_room_participant(chat_room_id, member_id);