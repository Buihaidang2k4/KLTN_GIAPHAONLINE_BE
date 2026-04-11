package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.AuditLogRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/audit-logs")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "AuditLog Management")
public class AuditLogController {
    AuditLogService auditLogService;

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogRes>>> getByFamily(
            @PathVariable Long familyId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_LOG_BY_FAMILY_SUCCESS", auditLogService.getByFamilyId(familyId, pageable)));
    }

    @GetMapping("/entity")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogRes>>> getByEntity(
            @RequestParam String entityType,
            @RequestParam String entityId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_LOG_BY_ENTITY", auditLogService.getByEntity(entityType, entityId, pageable)));
    }

    @GetMapping("/actor/{accountId}")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogRes>>> getByActor(
            @PathVariable Long accountId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_LOG_BY_ACTOR_SUCCESS", auditLogService.getByActor(accountId, pageable)));
    }
}
