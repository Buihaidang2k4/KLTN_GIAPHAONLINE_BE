package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;

@Builder
public record CreatePermissionReq(
        String name,
        String description
) {
}
