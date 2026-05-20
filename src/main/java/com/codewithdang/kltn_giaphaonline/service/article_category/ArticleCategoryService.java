package com.codewithdang.kltn_giaphaonline.service.article_category;


import com.codewithdang.kltn_giaphaonline.dto.request.CreateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateArticleCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ArticleCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ArticleCategoryService {
    ArticleCategoryRes createCategory(CreateArticleCategoryReq req);

    ArticleCategoryRes updateCategory(Long articleCategoryId, UpdateArticleCategoryReq req);

    void deleteCategory(Long articleCategoryId);

    ArticleCategoryRes getCategoryById(Long articleCategoryId);

    PageResponse<ArticleCategoryRes> getAll(String keyword, Pageable pageable);
}
