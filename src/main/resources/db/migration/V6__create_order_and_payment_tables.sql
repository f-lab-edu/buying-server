
-- Order 테이블 생성
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,

                        order_id VARCHAR(64) NOT NULL UNIQUE,
                        buyer_id BIGINT NOT NULL,

                        product_id BIGINT NOT NULL,
                        product_name VARCHAR(255) NOT NULL,
                        order_name VARCHAR(255) NOT NULL,

                        quantity INT NOT NULL,
                        total_amount BIGINT NOT NULL,

                        status VARCHAR(20) NOT NULL DEFAULT 'READY',

                        created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                        updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

                        CONSTRAINT fk_order_buyer
                            FOREIGN KEY (buyer_id) REFERENCES member(id)
);

-- 인덱스 추가
CREATE INDEX idx_order_buyer_id ON orders(buyer_id);
CREATE INDEX idx_order_order_id ON orders(order_id);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_created_at ON orders(created_at);


-- Payment 테이블 생성
CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                          pg_payment_id VARCHAR(200) NOT NULL UNIQUE,
                          pg_provider VARCHAR(30) NOT NULL,

                          order_id VARCHAR(64) NOT NULL,
                          order_pk_id BIGINT NOT NULL,

                          total_amount BIGINT NOT NULL,
                          method VARCHAR(50),

                          raw_data_json TEXT,

                          status VARCHAR(20) NOT NULL DEFAULT 'READY',

                          requested_at DATETIME,
                          approved_at DATETIME,

                          failure_code VARCHAR(100),
                          failure_message VARCHAR(500),

                          created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

                          CONSTRAINT fk_payment_order
                              FOREIGN KEY (order_pk_id) REFERENCES orders(id)
);

-- 인덱스 추가
CREATE INDEX idx_payment_order_id ON payments(order_id);
CREATE INDEX idx_payment_order_pk_id ON payments(order_pk_id);
CREATE INDEX idx_payment_pg_payment_id ON payments(pg_payment_id);
CREATE INDEX idx_payment_status ON payments(status);