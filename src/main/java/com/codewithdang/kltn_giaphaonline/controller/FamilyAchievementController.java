package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyAchievementRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.family_achievement.FamilyAchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/families/{familyId}/achievements")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Achievement Management")
public class FamilyAchievementController {

    FamilyAchievementService familyAchievementService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> create(
            @PathVariable Long familyId,
            @Valid @RequestPart("data") FamilyAchievementReq req,
            @RequestPart(value = "evidence", required = false) MultipartFile evidence
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(201, "CREATE_ACHIEVEMENT_SUCCESS",
                        familyAchievementService.create(familyId, req, evidence))
        );
    }

    @PutMapping("/{achievementId}")
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> update(
            @PathVariable Long familyId,
            @PathVariable Long achievementId,
            @Valid @RequestPart("data") UpdateFamilyAchievementReq req,
            @RequestPart(value = "evidence", required = false) MultipartFile evidence
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_ACHIEVEMENT_SUCCESS",
                        familyAchievementService.update(familyId, achievementId, req, evidence))
        );
    }

    @DeleteMapping("/{achievementId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long familyId,
            @PathVariable Long achievementId) {
        familyAchievementService.delete(familyId, achievementId);
        return ResponseEntity.ok(
                ApiResponse.success(200, "DELETE_ACHIEVEMENT_SUCCESS", null)
        );
    }

    @GetMapping("/{achievementId}")
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> getById(
            @PathVariable Long familyId,
            @PathVariable Long achievementId) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ACHIEVEMENT_SUCCESS",
                        familyAchievementService.getById(achievementId))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FamilyAchievementRes>>> getByFamily(
            @PathVariable Long familyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ACHIEVEMENTS_SUCCESS",
                        familyAchievementService.getByFamily(familyId, keyword, pageable))
        );
    }
}
