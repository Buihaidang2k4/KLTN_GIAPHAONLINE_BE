package com.codewithdang.kltn_giaphaonline.dto.response;

import java.util.Set;

public record RoleRes(
        String name,
        String description,
        Set<PermissionRes> permissions
) {
}
