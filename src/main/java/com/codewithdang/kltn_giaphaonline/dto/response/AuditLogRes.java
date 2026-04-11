package com.codewithdang.kltn_giaphaonline.dto.response;

import java.time.Instant;
import java.util.Map;

public record AuditLogRes(
        Long auditId,
        Long actorAccountId,
        String actorName,
        Long familyId,
        String action,
        String entityType,
        String entityId,
        Map<String, Object> oldData,
        Map<String, Object> newData,
        Instant createdAt
) {
}