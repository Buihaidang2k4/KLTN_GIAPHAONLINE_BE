package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreatePermissionReq(
        @NotBlank(message = "PERMISSION_NAME_REQUIRED")
        @Size(max = 50, message = "PERMISSION_NAME_TOO_LONG")
        String name,

        @NotBlank(message = "SCOPE_TYPE_REQUIRED")
        String scopeType,

        @Size(max = 255, message = "DESCRIPTION_TOO_LONG")
        String description
) {
}