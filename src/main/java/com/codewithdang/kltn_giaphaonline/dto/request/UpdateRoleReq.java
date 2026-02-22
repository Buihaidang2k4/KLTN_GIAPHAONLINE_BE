package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;

import java.util.Set;

@Builder
public record UpdateRoleReq(
        String description,
        Set<String> permissions
) {
}
