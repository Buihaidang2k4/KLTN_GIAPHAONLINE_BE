package com.codewithdang.kltn_giaphaonline.service.audit_log;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.entity.AuditLog;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.AuditLogMapper;
import com.codewithdang.kltn_giaphaonline.repo.AuditLogRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogServiceImpl implements AuditLogService {
    AuditLogRepo auditLogRepo;
    AuditLogMapper auditLogMapper;

    @Override
    @Transactional
    public void log(CreateAuditLogReq req) {
        if (req == null) return;

        if (req.action() == null || req.action().isBlank())
            throw new AppException(ErrorCode.ACTION_IS_EMPTY, "Audit action must not be blank");

        if (req.entityType() == null || req.entityType().isBlank())
            throw new AppException(ErrorCode.ENTITYTYPE_IS_EMPTY, "Audit entityType must not be blank");

        try {
            AuditLog auditLog = auditLogMapper.toEntity(req);
            auditLogRepo.save(auditLog);

            log.info("Audit logged: action={}, entityType={}, entityId={}",
                    req.action(), req.entityType(), req.entityId());
        } catch (Exception e) {
            log.error("Failed to write audit log. action={}, entityType={}, entityId={}",
                    req.action(), req.entityType(), req.entityId(), e);
            throw e;
        }
    }
}
