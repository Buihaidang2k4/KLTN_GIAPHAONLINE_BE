package com.codewithdang.kltn_giaphaonline.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnums {
    // SYSTEM PERMISSIONS (Dành cho Web Admin)
    SYS_DASHBOARD_VIEW("Xem thống kê hệ thống"),
    SYS_PLAN_MANAGE("Quản lý gói cước"),
    SYS_PAYMENT_MANAGE("Quản lý thanh toán"),
    SYS_ACCOUNT_MANAGE("Quản lý tài khoản người dùng"),
    SYS_CONTENT_MANAGE("Quản lý bài viết trang chủ"),

    // FAMILY PERMISSIONS (Dành cho dòng họ)
    FAM_DELETE("Xóa gia phả"),
    FAM_SETTINGS_EDIT("Chỉnh sửa thông tin dòng họ"),
    FAM_MEMBER_MANAGE("Quản lý thành viên trong họ"),
    FAM_SUBSCRIPTION_MANAGE("Quản lý gói cước dòng họ"),
    FAM_EXPORT("Xuất dữ liệu gia phả"),

    // DATA PERMISSIONS (Biên tập dữ liệu)
    NODE_WRITE("Thêm/Sửa thành viên cây"),
    NODE_DELETE("Xóa thành viên cây"),
    RELATIONSHIP_MANAGE("Quản lý quan hệ"),
    POST_MANAGE("Quản lý bài đăng dòng họ"),
    EVENT_MANAGE("Quản lý sự kiện/ngày giỗ"),
    ALBUM_MANAGE("Quản lý album ảnh"),

    // PUBLIC PERMISSIONS (Quyền xem)
    FAM_VIEW_PUBLIC("Xem gia phả công khai"),
    POST_VIEW_PUBLIC("Xem bài viết công khai");

    private final String description;
}