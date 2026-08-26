package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyAchievementRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.FamilyAchievement.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Achievement Management")
public class FamilyAchievementController {

    FamilyAchievementService familyAchievementService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> create(
            @PathVariable Long familyId,
            @Valid @RequestPart("data") FamilyAchievementReq req,
            @RequestPart(value = "evidence", required = false) MultipartFile evidence
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(201, MessageConstantsVi.FamilyAchievement.CREATE_ACHIEVEMENT_SUCCESS,
                        familyAchievementService.create(familyId, req, evidence))
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.FamilyAchievement.BY_ID)
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> update(
            @PathVariable Long familyId,
            @PathVariable Long achievementId,
            @Valid @RequestPart("data") UpdateFamilyAchievementReq req,
            @RequestPart(value = "evidence", required = false) MultipartFile evidence
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyAchievement.UPDATE_ACHIEVEMENT_SUCCESS,
                        familyAchievementService.update(familyId, achievementId, req, evidence))
        );
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyAchievement.BY_ID)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long familyId,
            @PathVariable Long achievementId) {
        familyAchievementService.delete(familyId, achievementId);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyAchievement.DELETE_ACHIEVEMENT_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyAchievement.BY_ID)
    public ResponseEntity<ApiResponse<FamilyAchievementRes>> getById(
            @PathVariable Long familyId,
            @PathVariable Long achievementId) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyAchievement.GET_ACHIEVEMENT_SUCCESS,
                        familyAchievementService.getById(achievementId))
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FamilyAchievementRes>>> getByFamily(
            @PathVariable Long familyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.FamilyAchievement.GET_ACHIEVEMENTS_SUCCESS,
                        familyAchievementService.getByFamily(familyId, keyword, pageable))
        );
    }
}
