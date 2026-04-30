package com.codewithdang.kltn_giaphaonline.service.family_post;

import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FamilyPostCategoryService {
    FamilyPostCategoryRes createPostCategory(Long familyId, PostCategoryReq req);

    FamilyPostCategoryRes updatePostCategory(Long familyId, Long postCategoryId, PostCategoryReq req);

    void deletePostCategory(Long familyId, Long postCategoryId);

    PageResponse<FamilyPostCategoryRes> getPostCategoriesByFamily(Long familyId, String keyword, Pageable pageable);

    PageResponse<FamilyPostCategoryRes> getPostCategories(Pageable pageable);

    FamilyPostCategoryRes getPostCategoryById(Long postCategoryId);
}
