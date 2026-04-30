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
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyAchievementServiceImpl implements FamilyAchievementService {

    FamilyAchievementRepo familyAchievementRepo;
    FamilyAchievementMapper familyAchievementMapper;
    FamilyRepo familyRepo;
    PageMapper pageMapper;
    MinioService minioService;

    @Override
    @Transactional
    public FamilyAchievementRes create(Long familyId, FamilyAchievementReq req, MultipartFile evidence) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        FamilyAchievement achievement = familyAchievementMapper.toEntity(req);
        achievement.setFamily(family);

        // upload evidence if exists
        if (evidence != null && !evidence.isEmpty()) {
            String path = minioService.uploadImage(evidence, ConstantUtils.Evidence);
            achievement.setEvidencePath(path);
        }
        achievement = familyAchievementRepo.save(achievement);
        if (achievement.getEvidencePath() != null)
            achievement.setEvidenceUrl(minioService.getPresignedUrl(achievement.getEvidencePath()));
        return familyAchievementMapper.toDto(achievement);
    }

    @Override
    @Transactional
    public FamilyAchievementRes update(Long familyId, Long achievementId, UpdateFamilyAchievementReq req, MultipartFile evidence) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));


        if (!achievement.getFamily().getFamilyId().equals(familyId))
            throw new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_IN_FAMILY);

        familyAchievementMapper.updateEntity(req, achievement);

        if (evidence != null && !evidence.isEmpty()) {
            // delete old evidence
            if (achievement.getEvidencePath() != null) {
                minioService.deleteFile(achievement.getEvidencePath());
            }

            // set new evidence
            String path = minioService.uploadImage(evidence, ConstantUtils.Evidence);
            achievement.setEvidencePath(path);
        }
        achievement = familyAchievementRepo.save(achievement);

        if (achievement.getEvidencePath() != null)
            achievement.setEvidenceUrl(minioService.getPresignedUrl(achievement.getEvidencePath()));

        return familyAchievementMapper.toDto(achievement);
    }

    @Override
    @Transactional
    public void delete(Long familyId, Long achievementId) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        // delete evidence if exists
        if (achievement.getEvidencePath() != null)
            minioService.deleteFile(achievement.getEvidencePath());

        if (!achievement.getFamily().getFamilyId().equals(familyId))
            throw new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_IN_FAMILY);

        familyAchievementRepo.delete(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyAchievementRes getById(Long achievementId) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        if (achievement.getEvidencePath() != null)
            achievement.setEvidenceUrl(minioService.getPresignedUrl(achievement.getEvidencePath()));

        return familyAchievementMapper.toDto(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyAchievementRes> getByFamily(Long familyId, String keyword, Pageable pageable) {
        Page<FamilyAchievement> page = familyAchievementRepo.findAllByFamily_FamilyIdAndKeyword(familyId, keyword, pageable);

        return pageMapper.toPageResponse(
                page,
                familyAchievement -> {
                    if (familyAchievement.getEvidencePath() != null)
                        familyAchievement.setEvidenceUrl(minioService.getPresignedUrl(familyAchievement.getEvidencePath()));

                    return familyAchievementMapper.toDto(familyAchievement);
                }
        );
    }
}
