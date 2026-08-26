package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.ArticleCategory.BASE)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Article Management")
public class ArticleCategoryController {
    ArticleCategoryService articleCategoryService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping
    ResponseEntity<ApiResponse<ArticleCategoryRes>> create(@RequestBody @Valid CreateArticleCategoryReq req) {
        return ResponseEntity.status(201).body(
                ApiResponse.success(201, MessageConstantsVi.ArticleCategory.CREATE_ARTICLE_CATEGORY_SUCCESS, articleCategoryService.createCategory(req))
        );
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(ApiPath.ArticleCategory.BY_ID)
    ResponseEntity<ApiResponse<ArticleCategoryRes>> update(@PathVariable Long articleCategoryId,
                                                           @RequestBody @Valid UpdateArticleCategoryReq req) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.ArticleCategory.UPDATE_ARTICLE_CATEGORY_SUCCESS, articleCategoryService.updateCategory(articleCategoryId, req))
        );
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.ArticleCategory.BY_ID)
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long articleCategoryId) {
        articleCategoryService.deleteCategory(articleCategoryId);
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.ArticleCategory.DELETE_ARTICLE_CATEGORY_SUCCESS, null)
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.ArticleCategory.BY_ID)
    ResponseEntity<ApiResponse<ArticleCategoryRes>> getById(@PathVariable Long articleCategoryId) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.ArticleCategory.GET_ARTICLE_CATEGORY_SUCCESS, articleCategoryService.getCategoryById(articleCategoryId))
        );
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<ArticleCategoryRes>>> getAll(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(200, MessageConstantsVi.ArticleCategory.GET_ALL_ARTICLE_CATEGORY_SUCCESS,
                        articleCategoryService.getAll(keyword, pageable))
        );
    }
}