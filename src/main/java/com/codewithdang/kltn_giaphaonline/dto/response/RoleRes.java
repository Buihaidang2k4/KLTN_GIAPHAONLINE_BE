package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record RoleRes(
        String name,
        String scopeType,
        String description,
        Set<PermissionRes> permissions
) {
}
