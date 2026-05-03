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
        String path = uploadEvidence(evidence);
        achievement.setEvidencePath(path);

        achievement = familyAchievementRepo.save(achievement);
        return toDtoWithEvidenceUrl(achievement);
    }

    @Override
    @Transactional
    public FamilyAchievementRes update(Long familyId, Long achievementId, UpdateFamilyAchievementReq req, MultipartFile evidence) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        if (!achievement.getFamily().getFamilyId().equals(familyId))
            throw new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_IN_FAMILY);

        familyAchievementMapper.updateEntity(req, achievement);

        if (hasFile(evidence)) {
            // delete old evidence
            deleteEvidenceIfExists(achievement);

            // set new evidence
            String path = uploadEvidence(evidence);
            achievement.setEvidencePath(path);
        }
        achievement = familyAchievementRepo.save(achievement);


        return toDtoWithEvidenceUrl(achievement);
    }

    @Override
    @Transactional
    public void delete(Long familyId, Long achievementId) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        // delete evidence if exists
        if (!achievement.getFamily().getFamilyId().equals(familyId))
            throw new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_IN_FAMILY);

        familyAchievementRepo.delete(achievement);
        deleteEvidenceIfExists(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyAchievementRes getById(Long achievementId) {
        FamilyAchievement achievement = familyAchievementRepo.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_ACHIEVEMENT_NOT_EXISTED));

        return toDtoWithEvidenceUrl(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyAchievementRes> getByFamily(Long familyId, String keyword, Pageable pageable) {
        Page<FamilyAchievement> page = familyAchievementRepo.findAllByFamily_FamilyIdAndKeyword(familyId, keyword, pageable);
        return pageMapper.toPageResponse(page, this::toDtoWithEvidenceUrl);
    }

    private FamilyAchievementRes toDtoWithEvidenceUrl(FamilyAchievement achievement) {
        FamilyAchievementRes res = familyAchievementMapper.toDto(achievement);

        if (achievement.getEvidencePath() != null) {
            res.setEvidenceUrl(
                    minioService.getPresignedUrl(achievement.getEvidencePath())
            );
        }

        return res;
    }

    private String uploadEvidence(MultipartFile file) {
        if (!hasFile(file)) return null;

        return minioService.uploadImage(file, ConstantUtils.Evidence);
    }

    private boolean hasFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private void deleteEvidenceIfExists(FamilyAchievement achievement) {
        if (achievement.getEvidencePath() != null) {
            minioService.deleteFile(achievement.getEvidencePath());
        }
    }


}
