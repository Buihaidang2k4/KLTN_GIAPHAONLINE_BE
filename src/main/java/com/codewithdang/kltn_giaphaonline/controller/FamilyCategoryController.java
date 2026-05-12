package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
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
@RequestMapping("${api.prefix}/family-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Category Management")
public class FamilyCategoryController {
    FamilyCategoryService familyCategoryService;

    @PostMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<FamilyCategoryRes>> createFamilyCategory(@PathVariable Long familyId, @Valid @RequestBody FamilyCategoryReq categoryReq) {
        return ResponseEntity.ok(ApiResponse.success(200, "CREATE_FAMILY_CATEGORY_SUCCESS",
                familyCategoryService.createFamilyCategory(familyId, categoryReq)));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<FamilyCategoryRes>> updateFamilyCategory(@PathVariable Long categoryId, @Valid @RequestBody FamilyCategoryReq categoryReq) {
        return ResponseEntity.ok(ApiResponse.success(200, "UPDATE_FAMILY_CATEGORY_SUCCESS",
                familyCategoryService.updateFamilyCategory(categoryId, categoryReq)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteFamilyCategory(@PathVariable Long categoryId) {
        familyCategoryService.deleteFamilyCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_FAMILY_CATEGORY_SUCCESS", null));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<FamilyCategoryRes>> getFamilyCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_FAMILY_CATEGORY_BY_ID_SUCCESS",
                familyCategoryService.getFamilyCategoryById(categoryId)));
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<PageResponse<FamilyCategoryRes>>> getAllCategoryByFamilyId(@PathVariable Long familyId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALL_CATEGORY_BY_FAMILY_ID_SUCCESS",
                familyCategoryService.getAllCategoryByFamilyId(familyId, pageable)));
    }
}
