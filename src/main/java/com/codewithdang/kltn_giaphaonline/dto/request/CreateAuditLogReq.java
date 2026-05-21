package com.codewithdang.kltn_giaphaonline.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CreateAuditLogReq {
    Long actorAccountId;
    Long familyId;
    String auditAction;
    String entityType;
    String entityId;
    Map<String, Object> oldData;
    Map<String, Object> newData;
    String ipAddress;
    String userAgent;
}