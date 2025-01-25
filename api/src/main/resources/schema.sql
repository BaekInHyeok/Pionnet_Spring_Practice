-- 상품 재고 테이블 생성
CREATE TABLE IF NOT EXISTS stock (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        item_id BIGINT NOT NULL,
        quantity INT NOT NULL,
        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);