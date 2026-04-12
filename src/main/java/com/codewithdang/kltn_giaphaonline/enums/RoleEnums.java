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
            "Quản trị viên dòng họ",
            RoleScopeType.FAMILY,
            Set.of(
                    FAM_VIEW_PUBLIC,
                    FAM_DELETE,
                    FAM_SETTINGS_EDIT,
                    FAM_MEMBER_MANAGE,
                    FAM_SUBSCRIPTION_MANAGE,
                    FAM_EXPORT,
                    NODE_WRITE,
                    NODE_DELETE,
                    RELATIONSHIP_MANAGE,
                    POST_MANAGE,
                    EVENT_MANAGE,
                    ALBUM_MANAGE,
                    CEREMONY_MANAGE
            )
    ),

    FAMILY_EDITOR(
            "Biên tập viên dòng họ",
            RoleScopeType.FAMILY,
            Set.of(
                    NODE_WRITE,
                    RELATIONSHIP_MANAGE,
                    POST_MANAGE,
                    EVENT_MANAGE,
                    ALBUM_MANAGE,
                    FAM_VIEW_PUBLIC
            )
    ),

    FAMILY_VIEWER(
            "Người xem",
            RoleScopeType.FAMILY,
            Set.of(
                    FAM_VIEW_PUBLIC,
                    POST_VIEW_PUBLIC
            )
    );

    private final String description;
    private final RoleScopeType scopeType;
    private final Set<PermissionEnums> permissionEnums;
}