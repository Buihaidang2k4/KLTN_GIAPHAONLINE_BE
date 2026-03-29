package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionEnums implements BaseEnum {

    // ================= SYSTEM =================
    SYS_DASHBOARD_VIEW("Xem thống kê hệ thống", RoleScopeType.SYSTEM),
    SYS_PLAN_MANAGE("Quản lý gói cước", RoleScopeType.SYSTEM),
    SYS_PAYMENT_MANAGE("Quản lý thanh toán", RoleScopeType.SYSTEM),
    SYS_ACCOUNT_MANAGE("Quản lý tài khoản", RoleScopeType.SYSTEM),
    SYS_CONTENT_MANAGE("Quản lý bài viết trang chủ", RoleScopeType.SYSTEM),

    // ================= FAMILY =================
    FAM_DELETE("Xóa gia phả", RoleScopeType.FAMILY),
    FAM_SETTINGS_EDIT("Chỉnh sửa thông tin dòng họ", RoleScopeType.FAMILY),
    FAM_MEMBER_MANAGE("Quản lý thành viên", RoleScopeType.FAMILY),
    FAM_SUBSCRIPTION_MANAGE("Quản lý gói cước dòng họ", RoleScopeType.FAMILY),
    FAM_EXPORT("Xuất dữ liệu gia phả", RoleScopeType.FAMILY),
    FAM_SUBSCRIPTION_VIEW("Xem gói hiện tại, dung lượng, hạn dùng", RoleScopeType.FAMILY),
    FAM_SUBSCRIPTION_UPGRADE_REQUEST("Gửi yêu cầu nâng cấp cho manager", RoleScopeType.FAMILY),
    FAM_PAYMENT_VIEW("xem lịch sử thanh toán", RoleScopeType.FAMILY),

    // ================= DATA =================
    NODE_WRITE("Thêm/Sửa thành viên cây", RoleScopeType.FAMILY),
    NODE_DELETE("Xóa thành viên cây", RoleScopeType.FAMILY),
    RELATIONSHIP_MANAGE("Quản lý quan hệ", RoleScopeType.FAMILY),
    POST_MANAGE("Quản lý bài đăng", RoleScopeType.FAMILY),
    EVENT_MANAGE("Quản lý sự kiện", RoleScopeType.FAMILY),
    ALBUM_MANAGE("Quản lý album ảnh", RoleScopeType.FAMILY),

    // ================= PUBLIC =================
    FAM_VIEW_PUBLIC("Xem gia phả", RoleScopeType.FAMILY),
    POST_VIEW_PUBLIC("Xem bài viết", RoleScopeType.FAMILY);

    private final String label;
    private final RoleScopeType scopeType;

    @Override
    public String getLabel() {
        return this.label;
    }
}