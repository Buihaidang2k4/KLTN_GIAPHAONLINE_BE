package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.tree.family_category.FamilyCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.FamilyCategory.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Category Management")
public class FamilyCategoryController {
    FamilyCategoryService familyCategoryService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.FamilyCategory.BY_FAMILY)
    public ResponseEntity<ApiResponse<FamilyCategoryRes>> createFamilyCategory(@PathVariable Long familyId, @Valid @RequestBody FamilyCategoryReq categoryReq) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyCategory.CREATE_FAMILY_CATEGORY_SUCCESS,
                familyCategoryService.createFamilyCategory(familyId, categoryReq)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.FamilyCategory.BY_ID)
    public ResponseEntity<ApiResponse<FamilyCategoryRes>> updateFamilyCategory(@PathVariable Long categoryId, @Valid @RequestBody FamilyCategoryReq categoryReq) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyCategory.UPDATE_FAMILY_CATEGORY_SUCCESS,
                familyCategoryService.updateFamilyCategory(categoryId, categoryReq)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyCategory.BY_ID)
    public ResponseEntity<ApiResponse<Void>> deleteFamilyCategory(@PathVariable Long categoryId) {
        familyCategoryService.deleteFamilyCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyCategory.DELETE_FAMILY_CATEGORY_SUCCESS, null));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyCategory.BY_ID)
    public ResponseEntity<ApiResponse<FamilyCategoryRes>> getFamilyCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyCategory.GET_FAMILY_CATEGORY_BY_ID_SUCCESS,
                familyCategoryService.getFamilyCategoryById(categoryId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyCategory.BY_FAMILY)
    public ResponseEntity<ApiResponse<PageResponse<FamilyCategoryRes>>> getAllCategoryByFamilyId(@PathVariable Long familyId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyCategory.GET_ALL_CATEGORY_BY_FAMILY_ID_SUCCESS,
                familyCategoryService.getAllCategoryByFamilyId(familyId, pageable)));
    }
}
