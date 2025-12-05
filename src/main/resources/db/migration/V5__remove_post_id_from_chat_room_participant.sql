-- =========================================
-- V5: ChatRoomParticipant에서 post_id 컬럼 제거
-- =========================================

-- 외래키 제약조건 제거
ALTER TABLE chat_room_participant
DROP FOREIGN KEY fk_participant_post;

-- 인덱스 제거
DROP INDEX idx_participant_post_id ON chat_room_participant;

-- 컬럼 제거
ALTER TABLE chat_room_participant
DROP COLUMN post_id;