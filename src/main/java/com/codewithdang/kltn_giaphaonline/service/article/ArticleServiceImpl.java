package com.codewithdang.kltn_giaphaonline.service.article;

import com.codewithdang.kltn_giaphaonline.dto.request.ArticleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Article;
import com.codewithdang.kltn_giaphaonline.entity.ArticleCategory;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.ArticleCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.ArticleRepo;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import com.codewithdang.kltn_giaphaonline.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleServiceImpl implements ArticleService {

    ArticleRepo articleRepo;
    ArticleCategoryRepo articleCategoryRepo;
    AccountRepo accountRepo;
    MinioService minioService;
    PageMapper pageMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleRes createArticle(ArticleReq req) {
        String slug = SlugUtil.toSlug(req.getTitle());
        if (articleRepo.existsBySlug(slug))
            slug = slug + "-" + System.currentTimeMillis();

        ArticleCategory category = null;
        if (req.getArticleCategoryId() != null) {
            category = articleCategoryRepo.findById(req.getArticleCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));
        }

        String thumbnailUrl = null;
        if (req.getThumbnail() != null && !req.getThumbnail().isEmpty()) {
            thumbnailUrl = minioService.uploadImage(req.getThumbnail(), ConstantUtils.Image);
        }

        Article article = Article.builder()
                .title(req.getTitle())
                .slug(slug)
                .summary(req.getSummary())
                .content(req.getContent())
                .thumbnailUrl(thumbnailUrl)
                .contentFormat(req.getContentFormat())
                .isFeatured(req.getIsFeatured() != null ? req.getIsFeatured() : false)
                .metaTitle(req.getMetaTitle())
                .metaDescription(req.getMetaDescription())
                .status(ArticleStatus.DRAFT)
                .articleCategory(category)
                .createdByAccount(getCurrentAccount())
                .build();

        return toRes(articleRepo.save(article));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleRes updateArticle(Long articleId, ArticleReq req) {
        Article article = getArticleOrThrow(articleId);

        if (req.getTitle() != null && !req.getTitle().equals(article.getTitle())) {
            String newSlug = SlugUtil.toSlug(req.getTitle());
            if (articleRepo.existsBySlug(newSlug) && !newSlug.equals(article.getSlug()))
                newSlug = newSlug + "-" + System.currentTimeMillis();
            article.setSlug(newSlug);
            article.setTitle(req.getTitle());
        }

        if (req.getThumbnail() != null && !req.getThumbnail().isEmpty()) {
            if (article.getThumbnailUrl() != null) minioService.deleteFile(article.getThumbnailUrl());
            article.setThumbnailUrl(minioService.uploadImage(req.getThumbnail(), ConstantUtils.Image));
        }

        if (req.getArticleCategoryId() != null) {
            ArticleCategory category = articleCategoryRepo.findById(req.getArticleCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));
            article.setArticleCategory(category);
        }

        if (req.getSummary() != null) article.setSummary(req.getSummary());
        if (req.getContent() != null) article.setContent(req.getContent());
        if (req.getContentFormat() != null) article.setContentFormat(req.getContentFormat());
        if (req.getIsFeatured() != null) article.setIsFeatured(req.getIsFeatured());
        if (req.getMetaTitle() != null) article.setMetaTitle(req.getMetaTitle());
        if (req.getMetaDescription() != null) article.setMetaDescription(req.getMetaDescription());

        return toRes(articleRepo.save(article));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public void deleteArticle(Long articleId) {
        Article article = getArticleOrThrow(articleId);
        articleRepo.delete(article);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleRes getById(Long articleId) {
        return toRes(getArticleOrThrow(articleId));
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleRes getBySlug(String slug) {
        Article article = articleRepo.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));
        article.setViewCount(article.getViewCount() + 1);
        articleRepo.save(article);
        return toRes(article);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ArticleRes> getAll(String keyword, ArticleStatus status, Long categoryId, Pageable pageable) {
        return pageMapper.toPageResponse(
                articleRepo.search(keyword, status, categoryId, pageable),
                this::toRes);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleRes publish(Long articleId) {
        Article article = getArticleOrThrow(articleId);
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedAt(Instant.now());
        return toRes(articleRepo.save(article));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleRes unpublish(Long articleId) {
        Article article = getArticleOrThrow(articleId);
        article.setStatus(ArticleStatus.DRAFT);
        article.setPublishedAt(null);
        return toRes(articleRepo.save(article));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleRes toggleFeatured(Long articleId) {
        Article article = getArticleOrThrow(articleId);
        article.setIsFeatured(!Boolean.TRUE.equals(article.getIsFeatured()));
        return toRes(articleRepo.save(article));
    }

    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        String path = minioService.uploadImage(file, ConstantUtils.Image);
        return minioService.getPresignedUrl(path);
    }

    // ==================== helpers ====================

    private Article getArticleOrThrow(Long articleId) {
        return articleRepo.findById(articleId)
                .filter(a -> a.getDeletedAt() == null)
                .orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));
    }

    private ArticleRes toRes(Article a) {
        String thumbnailUrl = a.getThumbnailUrl() != null ? minioService.getPresignedUrl(a.getThumbnailUrl()) : null;


        return ArticleRes.builder()
                .articleId(a.getArticleId())
                .slug(a.getSlug())
                .title(a.getTitle())
                .summary(a.getSummary())
                .thumbnailUrl(thumbnailUrl)
                .content(a.getContent())
                .viewCount(a.getViewCount())
                .isFeatured(a.getIsFeatured())
                .metaTitle(a.getMetaTitle())
                .metaDescription(a.getMetaDescription())
                .contentFormat(a.getContentFormat())
                .status(a.getStatus())
                .publishedAt(a.getPublishedAt())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .articleCategoryId(a.getArticleCategory() != null ? a.getArticleCategory().getArticleCategoryId() : null)
                .articleCategoryName(a.getArticleCategory() != null ? a.getArticleCategory().getName() : null)
                .createdByAccountId(a.getCreatedByAccount() != null ? a.getCreatedByAccount().getAccountId() : null)
                .createdByAccountName(a.getCreatedByAccount() != null ? a.getCreatedByAccount().getFullName() : null)
                .build();
    }

    private Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return accountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
