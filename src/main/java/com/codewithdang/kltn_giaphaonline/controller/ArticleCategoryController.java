package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.article_category.ArticleCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("${api.prefix}/article-categories")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Article Management")
public class ArticleCategoryController {
    ArticleCategoryService articleCategoryService;

    @PostMapping
    ResponseEntity<ApiResponse<ArticleCategoryRes>> create(@RequestBody @Valid CreateArticleCategoryReq req) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "CREATE_ARTICLE_CATEGORY_SUCCESS", articleCategoryService.createCategory(req))
        );
    }

    @PutMapping("/{articleCategoryId}")
    ResponseEntity<ApiResponse<ArticleCategoryRes>> update(@PathVariable Long articleCategoryId,
                                                           @RequestBody @Valid UpdateArticleCategoryReq req) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_ARTICLE_CATEGORY_SUCCESS", articleCategoryService.updateCategory(articleCategoryId, req))
        );
    }

    @DeleteMapping("/{articleCategoryId}")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long articleCategoryId) {
        articleCategoryService.deleteCategory(articleCategoryId);
        return ResponseEntity.ok(
                ApiResponse.success(200, "DELETE_ARTICLE_CATEGORY_SUCCESS", null)
        );
    }

    @GetMapping("/{articleCategoryId}")
    ResponseEntity<ApiResponse<ArticleCategoryRes>> getById(@PathVariable Long articleCategoryId) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ARTICLE_CATEGORY_SUCCESS", articleCategoryService.getCategoryById(articleCategoryId))
        );
    }

    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<ArticleCategoryRes>>> getAll(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ALL_ARTICLE_CATEGORY_SUCCESS", articleCategoryService.getAll(pageable))
        );
    }
}