package com.codewithdang.kltn_giaphaonline.service.family_achievement;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateFamilyAchievementReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyAchievementRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyAchievement;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyAchievementMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.FamilyAchievementRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyAchievementServiceImpl implements FamilyAchievementService {

    FamilyAchievementRepo familyAchievementRepo;
    FamilyAchievementMapper familyAchievementMapper;
    FamilyRepo familyRepo;
    PageMapper pageMapper;

    @Override
    @Transactional
    public FamilyAchievementRes create(Long familyId, FamilyAchievementReq req) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        FamilyAchievement achievement = familyAchievementMapper.toEntity(req);
        achievement.setFamily(family);

        return familyAchievementMapper.toDto(familyAchievementRepo.save(achievement));
    }

    @Override
    @Transactional
    public FamilyAchievementRes update(Long familyId, Long achievementId, UpdateFamilyAchievementReq req) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        if (!achievement.getFamily().getFamilyId().equals(familyId))
            throw new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_IN_FAMILY);

        familyAchievementMapper.updateEntity(req, achievement);

        return familyAchievementMapper.toDto(familyAchievementRepo.save(achievement));
    }

    @Override
    @Transactional
    public void delete(Long familyId, Long achievementId) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        if (!achievement.getFamily().getFamilyId().equals(familyId))
            throw new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_IN_FAMILY);

        familyAchievementRepo.delete(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyAchievementRes getById(Long achievementId) {
        return familyAchievementMapper.toDto(
                familyAchievementRepo.findById(achievementId)
                        .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyAchievementRes> getByFamily(Long familyId, String keyword, Pageable pageable) {
        return pageMapper.toPageResponse(
                familyAchievementRepo.findAllByFamily_FamilyIdAndKeyword(familyId, keyword, pageable),
                familyAchievementMapper::toDto
        );
    }
}
