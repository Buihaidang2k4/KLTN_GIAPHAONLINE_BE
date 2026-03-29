-- Kiểm tra nếu cột tồn tại thì mới xóa để tránh lỗi khi chạy lại
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name='articles' AND column_name='update_by_account_id') THEN

-- 2. Xóa cột thừa
ALTER TABLE articles DROP COLUMN IF EXISTS update_by_account_id;

END IF;
END $$;