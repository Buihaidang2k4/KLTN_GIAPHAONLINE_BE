package com.codewithdang.kltn_giaphaonline.dto.request;

import java.util.Set;

public record UpdateRoleReq(
        String description,
        Set<String> permissions
) {
}
