package com.codewithdang.kltn_giaphaonline.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.codewithdang.kltn_giaphaonline.enums.PermissionEnums.*;

@Getter
@RequiredArgsConstructor
public enum RoleEnums {
    // SYSTEM ROLES
    SYSTEM_ADMIN("Quản trị viên hệ thống - Toàn quyền điều hành", Set.of(
            SYS_DASHBOARD_VIEW, SYS_PLAN_MANAGE, SYS_PAYMENT_MANAGE, SYS_ACCOUNT_MANAGE, SYS_CONTENT_MANAGE
    )),

    SYSTEM_CONTENT_MANAGER("Quản lý nội dung hệ thống", Set.of(
            SYS_CONTENT_MANAGE
    )),

    // FAMILY ROLES
    FAMILY_ADMIN("Quản trị viên dòng họ - Có quyền xóa gia phả và quản lý thành viên", Set.of(
            FAM_DELETE, FAM_SETTINGS_EDIT, FAM_MEMBER_MANAGE, FAM_SUBSCRIPTION_MANAGE,
            FAM_EXPORT, NODE_WRITE, NODE_DELETE, RELATIONSHIP_MANAGE,
            POST_MANAGE, EVENT_MANAGE, ALBUM_MANAGE, FAM_VIEW_PUBLIC
    )),

    FAMILY_EDITOR("Biên tập viên dòng họ - Thêm sửa thông tin cây và bài viết", Set.of(
            NODE_WRITE, RELATIONSHIP_MANAGE, POST_MANAGE, EVENT_MANAGE, ALBUM_MANAGE, FAM_VIEW_PUBLIC
    )),

    FAMILY_VIEWER("Người xem - Chỉ có quyền xem thông tin công khai", Set.of(
            FAM_VIEW_PUBLIC, POST_VIEW_PUBLIC
    ));

    private final String description; // Thêm trường này
    private final Set<PermissionEnums> permissionEnums;
}