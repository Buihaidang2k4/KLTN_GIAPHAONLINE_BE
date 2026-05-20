package com.codewithdang.kltn_giaphaonline.service.article;

import com.codewithdang.kltn_giaphaonline.dto.request.ArticleReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    ArticleRes createArticle(ArticleReq req);

    ArticleRes updateArticle(Long articleId, ArticleReq req);

    void deleteArticle(Long articleId);

    ArticleRes getById(Long articleId);

    ArticleRes getBySlug(String slug);

    PageResponse<ArticleRes> getAll(String keyword, ArticleStatus status, Long categoryId, Pageable pageable);

    // publish / unpublish
    ArticleRes publish(Long articleId);

    ArticleRes unpublish(Long articleId);

    // toggle featured
    ArticleRes toggleFeatured(Long articleId);

    String uploadImage(MultipartFile file);
}
