package com.codewithdang.kltn_giaphaonline.dto.request;

import java.util.Map;

public record CreateAuditLogReq(
        Long actorAccountId,
        Long familyId,
        String action,
        String entityType,
        String entityId,
        Map<String, Object> oldData,
        Map<String, Object> newData,
        String ipAddress,
        String userAgent
) {
}