package com.codewithdang.kltn_giaphaonline.service.audit_log;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AuditLogRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;


public interface AuditLogService {

    void log(CreateAuditLogReq req);

    PageResponse<AuditLogRes> getByFamilyId(Long familyId, Pageable pageable);

    PageResponse<AuditLogRes> getByEntity(String entityType, String entityId, Pageable pageable);

    PageResponse<AuditLogRes> getByActor(Long actorAccountId, Pageable pageable);

}
