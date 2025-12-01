ALTER TABLE chat_room
    ADD CONSTRAINT uk_chat_room_post_attend_user
        UNIQUE (post_id, attend_user_id);