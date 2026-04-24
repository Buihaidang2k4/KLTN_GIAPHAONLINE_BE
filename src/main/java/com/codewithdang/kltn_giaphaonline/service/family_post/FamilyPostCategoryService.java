package com.codewithdang.kltn_giaphaonline.service.family_post;

import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;

public interface FamilyPostCategory {
    FamilyPostCategoryRes createPostCategory(Long familyId, PostCategoryReq req);

    FamilyPostCategoryRes updatePostCategory(Long familyId, PostCategoryReq req);

    void deletePostCategory(Long familyId);

    PageResponse<FamilyPostCategoryRes> getPostCategoriesByFamily(Long familyId);

    FamilyPostCategoryRes getPostCategoryById(Long postCategoryId);
}
