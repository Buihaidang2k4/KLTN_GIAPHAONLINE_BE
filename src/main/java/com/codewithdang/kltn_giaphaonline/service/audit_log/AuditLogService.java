package com.codewithdang.kltn_giaphaonline.service.audit_log;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.enums.AuditAction;

import java.util.Map;

public interface AuditLogService {
    void log(CreateAuditLogReq req);

}
