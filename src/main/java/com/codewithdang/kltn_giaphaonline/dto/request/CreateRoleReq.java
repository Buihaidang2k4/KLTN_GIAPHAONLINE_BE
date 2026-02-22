package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;

@Builder
public record CreateRoleReq(
        String name,
        String description
) {
}
