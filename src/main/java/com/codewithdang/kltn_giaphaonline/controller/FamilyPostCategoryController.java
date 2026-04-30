package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
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
@RequestMapping("${api.prefix}/family-post-categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyPostCategoryController {

    FamilyPostCategoryService postCategoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<FamilyPostCategoryRes>> createPostCategory(
            @RequestParam Long familyId,
            @RequestBody PostCategoryReq req
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        201,
                        "CREATE_POST_CATEGORY_SUCCESS",
                        postCategoryService.createPostCategory(familyId, req)
                )
        );
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<FamilyPostCategoryRes>> updatePostCategory(
            @RequestParam Long familyId,
            @PathVariable Long categoryId,
            @RequestBody PostCategoryReq req
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "UPDATE_POST_CATEGORY_SUCCESS",
                        postCategoryService.updatePostCategory(familyId, categoryId, req)
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deletePostCategory(
            @RequestParam Long familyId,
            @PathVariable Long categoryId
    ) {
        postCategoryService.deletePostCategory(familyId, categoryId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "DELETE_POST_CATEGORY_SUCCESS",
                        null
                )
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<FamilyPostCategoryRes>> getPostCategoryById(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "GET_POST_CATEGORY_SUCCESS",
                        postCategoryService.getPostCategoryById(categoryId)
                )
        );
    }

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
                        "GET_POST_CATEGORIES_BY_FAMILY_SUCCESS",
                        postCategoryService.getPostCategoriesByFamily(familyId, keyword, pageable)
                )
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<FamilyPostCategoryRes>>> getPostCategories(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "GET_ALL_POST_CATEGORIES_SUCCESS",
                        postCategoryService.getPostCategories(pageable)
                )
        );
    }

}