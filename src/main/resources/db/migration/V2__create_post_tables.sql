-- ================================
-- Buying Shop - Post, PostDetail, PostImage 테이블 생성
-- ================================

-- --------------------------------
-- 게시물 (post)
-- --------------------------------
CREATE TABLE post (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      member_id BIGINT NOT NULL,
                      title VARCHAR(100) NOT NULL,
                      price INT NOT NULL,
                      thumbnail_url VARCHAR(1000),
                      status VARCHAR(20) NOT NULL DEFAULT 'SELLING',
                      created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                      deleted_at DATETIME(6) NULL,
                      CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(id)
);

-- --------------------------------
-- 게시물 상세 내용 (post_detail)
-- --------------------------------
CREATE TABLE post_detail (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             post_id BIGINT NOT NULL,
                             content TEXT,
                             CONSTRAINT fk_post_detail_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- --------------------------------
-- 게시물 이미지 (post_image)
-- --------------------------------
CREATE TABLE post_image (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            post_id BIGINT NOT NULL,
                            image_url VARCHAR(1000) NOT NULL,
                            image_order INT NOT NULL,
                            CONSTRAINT fk_post_image_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- --------------------------------
-- 인덱스 추가 (조회 성능 최적화)
-- --------------------------------
CREATE INDEX idx_post_member_id ON post(member_id);
CREATE INDEX idx_post_image_post_id ON post_image(post_id);
CREATE INDEX idx_post_detail_post_id ON post_detail(post_id);