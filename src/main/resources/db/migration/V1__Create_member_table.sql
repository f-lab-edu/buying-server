-- ============================================
-- 🚀 Member 테이블 생성 (엔티티: org.example.buyingserver.member.domain.Member)
-- ============================================

CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,

                        email VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255),
                        nickname VARCHAR(50) NOT NULL,

                        status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                        social_type VARCHAR(20),
                        socialid VARCHAR(100),

                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        deleted_at DATETIME NULL
);


CREATE INDEX idx_member_socialid ON member(socialid);
