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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/families/{familyId}/achievements")
@RequiredArgsConstructor
@Tag(name = "Family Achievement Management")
public class FamilyAchievementController {

    private final FamilyAchievementService familyAchievementService;

    @PostMapping
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> create(
            @PathVariable Long familyId,
            @Valid @RequestBody FamilyAchievementReq req) {
        return ResponseEntity.ok(
                ApiResponse.success(201, "CREATE_ACHIEVEMENT_SUCCESS",
                        familyAchievementService.create(familyId, req))
        );
    }

    @PutMapping("/{achievementId}")
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> update(
            @PathVariable Long familyId,
            @PathVariable Long achievementId,
            @Valid @RequestBody UpdateFamilyAchievementReq req) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_ACHIEVEMENT_SUCCESS",
                        familyAchievementService.update(familyId, achievementId, req))
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
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ACHIEVEMENTS_SUCCESS",
                        familyAchievementService.getByFamily(familyId, pageable))
        );
    }
}
