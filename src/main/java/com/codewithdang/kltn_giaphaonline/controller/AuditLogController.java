package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.AuditLogRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.AuditLog.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "AuditLog Management")
public class AuditLogController {
    AuditLogService auditLogService;

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.AuditLog.BY_FAMILY)
    public ResponseEntity<ApiResponse<PageResponse<AuditLogRes>>> getByFamily(
            @PathVariable Long familyId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.AuditLog.GET_LOG_BY_FAMILY_SUCCESS, auditLogService.getByFamilyId(familyId, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.AuditLog.BY_ENTITY)
    public ResponseEntity<ApiResponse<PageResponse<AuditLogRes>>> getByEntity(
            @RequestParam String entityType,
            @RequestParam String entityId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.AuditLog.GET_LOG_BY_ENTITY_SUCCESS, auditLogService.getByEntity(entityType, entityId, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.AuditLog.BY_ACTOR)
    public ResponseEntity<ApiResponse<PageResponse<AuditLogRes>>> getByActor(
            @PathVariable Long accountId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.AuditLog.GET_LOG_BY_ACTOR_SUCCESS, auditLogService.getByActor(accountId, pageable)));
    }
}
