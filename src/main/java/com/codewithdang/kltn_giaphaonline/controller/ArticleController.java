package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.ArticleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
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
@RequestMapping("${api.prefix}/articles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Article Management")
public class ArticleController {

    ArticleService articleService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ArticleRes>> createArticle(@Valid @ModelAttribute ArticleReq req) {
        return ResponseEntity.status(201).body(ApiResponse.success(201, "CREATE_ARTICLE_SUCCESS",
                articleService.createArticle(req)));
    }

    @PutMapping(value = "/{articleId}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ArticleRes>> updateArticle(
            @PathVariable Long articleId,
            @ModelAttribute ArticleReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, "UPDATE_ARTICLE_SUCCESS",
                articleService.updateArticle(articleId, req)));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_ARTICLE_SUCCESS", null));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleRes>> getById(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ARTICLE_SUCCESS",
                articleService.getById(articleId)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ArticleRes>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ARTICLE_SUCCESS",
                articleService.getBySlug(slug)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ArticleRes>>> getAll(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) ArticleStatus status,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALL_ARTICLES_SUCCESS",
                articleService.getAll(keyword, status, categoryId, pageable)));
    }

    @PatchMapping("/{articleId}/publish")
    public ResponseEntity<ApiResponse<ArticleRes>> publish(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, "PUBLISH_ARTICLE_SUCCESS",
                articleService.publish(articleId)));
    }

    @PatchMapping("/{articleId}/unpublish")
    public ResponseEntity<ApiResponse<ArticleRes>> unpublish(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, "UNPUBLISH_ARTICLE_SUCCESS",
                articleService.unpublish(articleId)));
    }

    @PatchMapping("/{articleId}/toggle-featured")
    public ResponseEntity<ApiResponse<ArticleRes>> toggleFeatured(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success(200, "TOGGLE_FEATURED_SUCCESS",
                articleService.toggleFeatured(articleId)));
    }

    @PostMapping(value = "/upload-image", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        String url = articleService.uploadImage(file);
        return ResponseEntity.ok(ApiResponse.success(200, "UPLOAD_IMAGE_SUCCESS",
                Map.of("url", url)));
    }
}
