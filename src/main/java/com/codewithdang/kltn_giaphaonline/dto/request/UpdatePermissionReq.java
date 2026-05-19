package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdatePermissionReq(
        @NotBlank(message = "SCOPE_TYPE_REQUIRED")
        String scopeType,

        @Size(max = 255, message = "DESCRIPTION_TOO_LONG")
        String description
) {
}
