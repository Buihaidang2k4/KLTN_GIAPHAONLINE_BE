package com.codewithdang.kltn_giaphaonline.service.audit_log;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AuditLogRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.AuditLog;
import com.codewithdang.kltn_giaphaonline.enums.AuditEntityType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.AuditLogMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AuditLogRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogServiceImpl implements AuditLogService {
    AuditLogRepo auditLogRepo;
    AuditLogMapper auditLogMapper;
    PageMapper pageMapper;

    @Override
    @Transactional
    public void log(CreateAuditLogReq req) {
        if (req == null) return;

        if (req.getAuditAction() == null || req.getAuditAction().isBlank()) {
            log.warn("Audit action is empty, skipping log");
            return;
        }

        // entityType mặc định là FAMILY nếu không truyền
        if (req.getEntityType() == null || req.getEntityType().isBlank()) {
            req.setEntityType(AuditEntityType.FAMILY.name());
        }

        try {
            AuditLog auditLog = auditLogMapper.toEntity(req);
            auditLogRepo.save(auditLog);

            log.info("Audit logged: action={}, entityType={}, entityId={}",
                    req.getAuditAction(), req.getEntityType(), req.getEntityType());
        } catch (Exception e) {
            log.error("Failed to write audit log. action={}, entityType={}, entityId={}",
                    req.getAuditAction(), req.getEntityType(), req.getEntityId(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogRes> getByFamilyId(Long familyId, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepo.findByFamilyIdOrderByCreatedAtDesc(familyId, pageable);
        return pageMapper.toPageResponse(
                auditLogPage, auditLogMapper::toRes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogRes> getByEntity(String entityType, String entityId, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepo.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId, pageable);
        return pageMapper.toPageResponse(
                auditLogPage, auditLogMapper::toRes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogRes> getByActor(Long actorAccountId, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepo.findByActor_AccountIdOrderByCreatedAtDesc(actorAccountId, pageable);
        return pageMapper.toPageResponse(
                auditLogPage, auditLogMapper::toRes
        );
    }


}
