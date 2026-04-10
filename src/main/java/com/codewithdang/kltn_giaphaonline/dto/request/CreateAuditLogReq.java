package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.AuditAction;

import java.util.Map;

public record CreateAuditLogReq(
        Long actorAccountId,
        Long familyId,
        AuditAction action,
        String entityType,
        String entityId,
        Map<String, Object> oldData,
        Map<String, Object> newData,
        String ipAddress,
        String userAgent
) {
}