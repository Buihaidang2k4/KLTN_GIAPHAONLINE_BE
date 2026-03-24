-- ==========================================
-- 1. CẬP NHẬT BẢNG ROLES
-- ==========================================

-- A. Thêm cột mới (cho phép NULL để tránh lỗi dữ liệu cũ)
ALTER TABLE roles ADD COLUMN IF NOT EXISTS scope_type VARCHAR(20);

-- B. Cập nhật dữ liệu mặc định (tất cả Role cũ coi là SYSTEM)
UPDATE roles SET scope_type = 'SYSTEM' WHERE scope_type IS NULL;

-- C. Ép NOT NULL sau khi đã có dữ liệu
ALTER TABLE roles ALTER COLUMN scope_type SET NOT NULL;

-- D. Thêm ràng buộc Check (đảm bảo chỉ nhập SYSTEM hoặc FAMILY)
ALTER TABLE roles ADD CONSTRAINT check_roles_scope_type
    CHECK (scope_type IN ('SYSTEM', 'FAMILY'));


-- ==========================================
-- 2. CẬP NHẬT BẢNG PERMISSIONS
-- ==========================================

-- A. Thêm cột mới
ALTER TABLE permissions ADD COLUMN IF NOT EXISTS scope_type VARCHAR(20);

-- B. Cập nhật dữ liệu mặc định
UPDATE permissions SET scope_type = 'SYSTEM' WHERE scope_type IS NULL;

-- C. Ép NOT NULL
ALTER TABLE permissions ALTER COLUMN scope_type SET NOT NULL;

-- D. Thêm ràng buộc Check
ALTER TABLE permissions ADD CONSTRAINT check_permissions_scope_type
    CHECK (scope_type IN ('SYSTEM', 'FAMILY'));