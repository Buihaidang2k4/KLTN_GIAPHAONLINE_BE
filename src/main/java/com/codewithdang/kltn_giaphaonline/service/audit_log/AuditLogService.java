package com.codewithdang.kltn_giaphaonline.service.audit_log;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;

public interface AuditLogService {
    void log(CreateAuditLogReq req);
}
