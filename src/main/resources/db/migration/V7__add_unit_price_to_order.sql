ALTER TABLE orders ADD COLUMN unit_price BIGINT NULL;

UPDATE orders
SET unit_price = total_amount / quantity
WHERE unit_price IS NULL;

ALTER TABLE orders MODIFY COLUMN unit_price BIGINT NOT NULL;