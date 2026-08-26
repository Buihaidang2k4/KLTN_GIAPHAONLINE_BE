package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.ArticleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
import com.codewithdang.kltn_giaphaonline.service.article.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(ApiPath.Article.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Article Management")
public class ArticleController {

    ArticleService articleService;

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ArticleRes>> createArticle(@Valid @ModelAttribute ArticleReq req) {
        return ResponseEntity.status(201).body(ApiResponse.success(201, MessageConstantsVi.Article.CREATE_ARTICLE_SUCCESS,
                articleService.createArticle(req)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(value = ApiPath.Article.BY_ID, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ArticleRes>> updateArticle(
            @PathVariable Long articleId,
            @ModelAttribute ArticleReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.UPDATE_ARTICLE_SUCCESS,
                articleService.updateArticle(articleId, req)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.Article.BY_ID)
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.DELETE_ARTICLE_SUCCESS, null));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Article.BY_ID)
    public ResponseEntity<ApiResponse<ArticleRes>> getById(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.GET_ARTICLE_SUCCESS,
                articleService.getById(articleId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.Article.BY_SLUG)
    public ResponseEntity<ApiResponse<ArticleRes>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.GET_ARTICLE_SUCCESS,
                articleService.getBySlug(slug)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ArticleRes>>> getAll(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) ArticleStatus status,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.GET_ALL_ARTICLES_SUCCESS,
                articleService.getAll(keyword, status, categoryId, pageable)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.Article.PUBLISH)
    public ResponseEntity<ApiResponse<ArticleRes>> publish(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.PUBLISH_ARTICLE_SUCCESS,
                articleService.publish(articleId)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.Article.UNPUBLISH)
    public ResponseEntity<ApiResponse<ArticleRes>> unpublish(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.UNPUBLISH_ARTICLE_SUCCESS,
                articleService.unpublish(articleId)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PatchMapping(ApiPath.Article.TOGGLE_FEATURED)
    public ResponseEntity<ApiResponse<ArticleRes>> toggleFeatured(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.TOGGLE_FEATURED_SUCCESS,
                articleService.toggleFeatured(articleId)));
    }

    @OperatorAction(CommonEnums.Operator.IMPORT)
    @PostMapping(value = ApiPath.Article.UPLOAD_IMAGE, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        String url = articleService.uploadImage(file);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.Article.UPLOAD_IMAGE_SUCCESS,
                Map.of("url", url)));
    }
}
