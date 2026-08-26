package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.family_post.FamilyPostCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.FamilyPostCategory.BASE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyPostCategoryController {

    FamilyPostCategoryService postCategoryService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    public ResponseEntity<ApiResponse<FamilyPostCategoryRes>> createPostCategory(
            @RequestParam Long familyId,
            @RequestBody PostCategoryReq req
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        201,
                        MessageConstantsVi.FamilyPostCategory.CREATE_POST_CATEGORY_SUCCESS,
                        postCategoryService.createPostCategory(familyId, req)
                )
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.FamilyPostCategory.BY_ID)
    public ResponseEntity<ApiResponse<FamilyPostCategoryRes>> updatePostCategory(
            @RequestParam Long familyId,
            @PathVariable Long categoryId,
            @RequestBody PostCategoryReq req
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        MessageConstantsVi.FamilyPostCategory.UPDATE_POST_CATEGORY_SUCCESS,
                        postCategoryService.updatePostCategory(familyId, categoryId, req)
                )
        );
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyPostCategory.BY_ID)
    public ResponseEntity<ApiResponse<Void>> deletePostCategory(
            @RequestParam Long familyId,
            @PathVariable Long categoryId
    ) {
        postCategoryService.deletePostCategory(familyId, categoryId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        MessageConstantsVi.FamilyPostCategory.DELETE_POST_CATEGORY_SUCCESS,
                        null
                )
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyPostCategory.BY_ID)
    public ResponseEntity<ApiResponse<FamilyPostCategoryRes>> getPostCategoryById(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        MessageConstantsVi.FamilyPostCategory.GET_POST_CATEGORY_SUCCESS,
                        postCategoryService.getPostCategoryById(categoryId)
                )
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FamilyPostCategoryRes>>> getPostCategoriesByFamily(
            @RequestParam Long familyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        MessageConstantsVi.FamilyPostCategory.GET_POST_CATEGORIES_BY_FAMILY_SUCCESS,
                        postCategoryService.getPostCategoriesByFamily(familyId, keyword, pageable)
                )
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyPostCategory.ALL)
    public ResponseEntity<ApiResponse<PageResponse<FamilyPostCategoryRes>>> getPostCategories(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        MessageConstantsVi.FamilyPostCategory.GET_ALL_POST_CATEGORIES_SUCCESS,
                        postCategoryService.getPostCategories(pageable)
                )
        );
    }

}