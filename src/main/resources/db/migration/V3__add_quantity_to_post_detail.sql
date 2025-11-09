-- ================================
--Buying Shop - Add quantity column to post_detail
-- ================================

ALTER TABLE post_detail
    ADD COLUMN quantity INT NOT NULL DEFAULT 1 AFTER content;