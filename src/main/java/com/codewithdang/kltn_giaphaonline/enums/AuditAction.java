package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditAction implements BaseEnum {
    // family inviter
    INVITE_MEMBER("Mời thành viên"),
    ACCEPT_INVITATION("Chấp nhận lời mời"),
    REJECT_INVITATION("Từ chối lời mời"),

    // Hệ thống & Tài khoản
    ACCOUNT_CREATE("Tạo tài khoản"),
    ACCOUNT_UPDATE("Cập nhật tài khoản"),
    ACCOUNT_DELETE("Xóa tài khoản"),
    AUTH_LOGIN("Đăng nhập"),
    AUTH_LOGOUT("Đăng xuất"),

    // Quản lý thành viên trong dòng họ (Family Membership)
    ADD_FAMILY_MEMBER("Thêm thành viên gia phả"),
    UPDATE_FAMILY_MEMBER_ROLE("Cập nhật vai trò thành viên"),
    REMOVE_FAMILY_MEMBER("Xóa thành viên gia phả"),

    // Quản lý thông tin dòng họ (Family Management)
    FAMILY_CREATE("Tạo dòng họ mới"),
    FAMILY_UPDATE("Sửa thông tin dòng họ"),
    FAMILY_DELETE("Xóa dòng họ"),

    // Quản lý cây gia phả (Family Tree/Nodes)
    NODE_CREATE("Thêm người vào cây"),
    NODE_UPDATE("Sửa thông tin người"),
    NODE_DELETE("Xóa người khỏi cây"),
    NODE_RELATION_CHANGE("Thay đổi quan hệ"),

    // Giao dịch & Thanh toán (Gói dịch vụ)
    PAYMENT_SUCCESS("Thanh toán thành công"),
    PAYMENT_FAILED("Thanh toán thất bại"),
    SUBSCRIPTION_CANCEL("Hủy gói dịch vụ");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}