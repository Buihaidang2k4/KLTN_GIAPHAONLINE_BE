package com.codewithdang.kltn_giaphaonline.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.codewithdang.kltn_giaphaonline.enums.PermissionEnums.*;

@Getter
@RequiredArgsConstructor
public enum RoleEnums {

    // ================= SYSTEM =================
    SYSTEM_ADMIN(
            "Quản trị viên hệ thống - Toàn quyền điều hành",
            RoleScopeType.SYSTEM,
            Set.of(
                    SYS_DASHBOARD_VIEW,
                    SYS_PLAN_MANAGE,
                    SYS_PAYMENT_MANAGE,
                    SYS_ACCOUNT_MANAGE,
                    SYS_CONTENT_MANAGE
            )
    ),

    SYSTEM_CONTENT_MANAGER(
            "Quản lý nội dung hệ thống",
            RoleScopeType.SYSTEM,
            Set.of(SYS_CONTENT_MANAGE)
    ),

    // ================= FAMILY =================
    FAMILY_ADMIN(
            "Quản trị viên dòng họ - Toàn quyền quản lý gia phả",
            RoleScopeType.FAMILY,
            Set.of(
                    // Quyền quản trị
                    FAM_DELETE,
                    FAM_WRITE,
                    FAM_SETTINGS_EDIT,
                    FAM_MEMBER_MANAGE,
                    FAM_SUBSCRIPTION_MANAGE,
                    FAM_EXPORT,
                    // Quyền trên cây
                    NODE_WRITE,
                    NODE_DELETE,
                    RELATIONSHIP_MANAGE,
                    // Quản lý nội dung
                    POST_MANAGE,
                    EVENT_MANAGE,
                    ALBUM_MANAGE,
                    CEREMONY_MANAGE,
                    ACHIEVEMENT_MANAGE,
                    // Quyền xem thông tin
                    SETTINGS_VIEW,
                    FAM_SUBSCRIPTION_VIEW,
                    FAM_VIEW_PUBLIC,
                    POST_VIEW_PUBLIC,
                    ALBUM_VIEW_PUBLIC,
                    EVENT_VIEW_PUBLIC,
                    ACHIEVEMENT_VIEW_PUBLIC,
                    CEREMONY_VIEW_PUBLIC,
                    // Yêu cầu nâng cấp
                    FAM_SUBSCRIPTION_UPGRADE_REQUEST
            )
    ),

    FAMILY_EDITOR(
            "Biên tập viên dòng họ - Thêm/sửa/xóa nội dung, không quản lý thành viên hay gói cước",
            RoleScopeType.FAMILY,
            Set.of(
                    // Quyền chỉnh sửa dữ liệu
                    NODE_WRITE,
                    NODE_DELETE,
                    RELATIONSHIP_MANAGE,
                    POST_MANAGE,
                    EVENT_MANAGE,
                    ALBUM_MANAGE,
                    ACHIEVEMENT_MANAGE,
                    // Quyền xuất dữ liệu
                    FAM_EXPORT,
                    // Quyền xem thông tin
                    SETTINGS_VIEW,
                    FAM_SUBSCRIPTION_VIEW,
                    FAM_VIEW_PUBLIC,
                    POST_VIEW_PUBLIC,
                    ALBUM_VIEW_PUBLIC,
                    EVENT_VIEW_PUBLIC,
                    ACHIEVEMENT_VIEW_PUBLIC,
                    CEREMONY_VIEW_PUBLIC,
                    // Yêu cầu nâng cấp
                    FAM_SUBSCRIPTION_UPGRADE_REQUEST
            )
    ),

    FAMILY_VIEWER(
            "Người xem - Chỉ xem nội dung, có thể gửi yêu cầu nâng cấp",
            RoleScopeType.FAMILY,
            Set.of(
                    // Quyền xem công khai
                    FAM_VIEW_PUBLIC,
                    POST_VIEW_PUBLIC,
                    ALBUM_VIEW_PUBLIC,
                    EVENT_VIEW_PUBLIC,
                    ACHIEVEMENT_VIEW_PUBLIC,
                    CEREMONY_VIEW_PUBLIC,
                    // Quyền xem thông tin dòng họ và gói cước
                    SETTINGS_VIEW,
                    FAM_SUBSCRIPTION_VIEW,
                    // Yêu cầu nâng cấp
                    FAM_SUBSCRIPTION_UPGRADE_REQUEST
            )
    );

    private final String description;
    private final RoleScopeType scopeType;
    private final Set<PermissionEnums> permissionEnums;
}