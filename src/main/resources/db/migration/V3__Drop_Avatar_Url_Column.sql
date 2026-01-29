-- Kiểm tra nếu cột tồn tại thì mới xóa để tránh lỗi khi chạy lại
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name='accounts' AND column_name='avatar_url') THEN

        -- 1. Nếu cần, hãy đảm bảo dữ liệu từ avatar_url đã được backup sang avatar_path
UPDATE accounts SET avatar_path = avatar_url WHERE avatar_path IS NULL;

-- 2. Xóa cột thừa
ALTER TABLE accounts DROP COLUMN avatar_url;

END IF;
END $$;