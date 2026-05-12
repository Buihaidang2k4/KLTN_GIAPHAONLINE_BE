package com.codewithdang.kltn_giaphaonline.service.tree.family_category;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FamilyCategoryService {
    FamilyCategoryRes createFamilyCategory(Long familyId, FamilyCategoryReq req);

    FamilyCategoryRes updateFamilyCategory(Long categoryId, FamilyCategoryReq req);

    void deleteFamilyCategory(Long categoryId);

    FamilyCategoryRes getFamilyCategoryById(Long categoryId);

    PageResponse<FamilyCategoryRes> getAllCategoryByFamilyId(Long familyId, Pageable pageable);
}
