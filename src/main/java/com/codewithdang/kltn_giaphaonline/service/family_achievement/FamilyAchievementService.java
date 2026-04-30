package com.codewithdang.kltn_giaphaonline.service.family_achievement;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyAchievementRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FamilyAchievementService {
    FamilyAchievementRes create(Long familyId, FamilyAchievementReq req);

    FamilyAchievementRes update(Long familyId, Long achievementId, UpdateFamilyAchievementReq req);

    void delete(Long familyId, Long achievementId);

    FamilyAchievementRes getById(Long achievementId);

    PageResponse<FamilyAchievementRes> getByFamily(Long familyId, String keyword, Pageable pageable);
}
