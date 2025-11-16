-- --------------------------------
-- 게시물 (post)
-- --------------------------------
CREATE TABLE post (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      member_id BIGINT NOT NULL,
                      title VARCHAR(100) NOT NULL,
                      content TEXT,
                      price INT NOT NULL,
                      quantity INT NOT NULL,
                      thumbnail_url TEXT,
                      status VARCHAR(20) NOT NULL DEFAULT 'SELLING',
                      created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                      deleted_at DATETIME(6) NULL,
                      CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(id)
);

-- --------------------------------
-- 게시물 이미지 (post_image)
-- --------------------------------
CREATE TABLE post_image (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            post_id BIGINT NOT NULL,
                            image_url TEXT NOT NULL,
                            image_order INT NOT NULL,
                            created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                            CONSTRAINT fk_post_image_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- --------------------------------
-- 인덱스 추가 (조회 성능 최적화)
-- --------------------------------
CREATE INDEX idx_post_member_id ON post(member_id);
CREATE INDEX idx_post_image_post_id ON post_image(post_id);
