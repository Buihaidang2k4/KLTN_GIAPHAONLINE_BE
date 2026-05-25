package com.codewithdang.kltn_giaphaonline.service.article_category;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.ArticleCategory;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.ArticleCategoryMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.ArticleCategoryRepo;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import com.codewithdang.kltn_giaphaonline.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleCategoryServiceImpl implements ArticleCategoryService {
    ArticleCategoryRepo articleCategoryRepo;
    PageMapper pageMapper;
    ArticleCategoryMapper articleCategoryMapper;
    MinioService minioService;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleCategoryRes createCategory(CreateArticleCategoryReq req) {

        if (articleCategoryRepo.existsByName(req.getName()))
            throw new AppException(ErrorCode.ARTICLE_CATEGORY_EXISTED);

        ArticleCategory articleCategory = articleCategoryMapper.toEntity(req);
        articleCategory.setSlug(SlugUtil.toSlug(req.getName()));
        articleCategory.setDescription(req.getDescription());

        articleCategory = articleCategoryRepo.save(articleCategory);
        return articleCategoryMapper.toRes(articleCategory);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public ArticleCategoryRes updateCategory(Long articleCategoryId, UpdateArticleCategoryReq req) {
        ArticleCategory articleCategory = articleCategoryRepo.findById(articleCategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));

        if (articleCategoryRepo.existsByNameAndArticleCategoryIdNot(req.getName(), articleCategoryId))
            throw new AppException(ErrorCode.ARTICLE_CATEGORY_EXISTED);

        String slug = SlugUtil.toSlug(req.getName());
        if (articleCategoryRepo.existsBySlugAndArticleCategoryIdNot(slug, articleCategoryId))
            throw new AppException(ErrorCode.ARTICLE_CATEGORY_SLUG_EXISTED);

        articleCategory.setName(req.getName());
        articleCategory.setSlug(slug);
        articleCategory.setDescription(req.getDescription());
        articleCategory.setDisplayOrder(req.getDisplayOrder());

        articleCategory = articleCategoryRepo.save(articleCategory);
        return articleCategoryMapper.toRes(articleCategory);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SYS_CONTENT_MANAGE')")
    public void deleteCategory(Long articleCategoryId) {
        ArticleCategory articleCategory = articleCategoryRepo.findById(articleCategoryId).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));

        if (articleCategory.getArticles() != null && !articleCategory.getArticles().isEmpty())
            throw new AppException(ErrorCode.ARTICLE_CATEGORY_ALREADY_HAS_ARTICLES);

        articleCategoryRepo.delete(articleCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleCategoryRes getCategoryById(Long articleCategoryId) {
        ArticleCategory articleCategory = articleCategoryRepo.findById(articleCategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_EXISTED));

        return articleCategoryMapper.toRes(articleCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ArticleCategoryRes> getAll(String keyword, Pageable pageable) {
        return pageMapper.toPageResponse(
                articleCategoryRepo.searchByName(keyword, pageable),
                articleCategoryMapper::toRes);
    }
}
