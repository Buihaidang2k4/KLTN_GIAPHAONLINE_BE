package com.codewithdang.kltn_giaphaonline.service.dashboard;

import com.codewithdang.kltn_giaphaonline.dto.response.DashboardRes;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.enums.FamilyMemberStatus;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    FamilyRepo familyRepo;
    FamilyCategoryRepo familyCategoryRepo;
    FamilyEventRepo familyEventRepo;
    AlbumRepo albumRepo;
    FamilyAchievementRepo familyAchievementRepo;
    FamilyMemberRepo familyMemberRepo;
    FamilySubscriptionRepo familySubscriptionRepo;
    AlbumMediaRepo albumMediaRepo;
    CeremonyRepo ceremonyRepo;


    /***
     * user
     * @param familyId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public DashboardRes getDataDashboard(Long familyId) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        // subscription hiện tại
        Optional<FamilySubscription> subOpt = familySubscriptionRepo
                .findFirstByFamilyAndStatusOrderByCreatedAtDesc(family, SubscriptionStatus.ACTIVE);

        String planName = null;
        String planPrice = null;
        java.time.LocalDate startDate = null;
        java.time.LocalDate endDate = null;
        Long maxStorageMb = null;

        if (subOpt.isPresent()) {
            FamilySubscription sub = subOpt.get();
            if (sub.getSubscriptionPlan() != null) {
                planName = sub.getSubscriptionPlan().getNamePlan();
                planPrice = sub.getSubscriptionPlan().getPrice() != null
                        ? sub.getSubscriptionPlan().getPrice().toPlainString()
                        : null;
                maxStorageMb = sub.getSubscriptionPlan().getMaxStorageMb() != null
                        ? sub.getSubscriptionPlan().getMaxStorageMb().longValue()
                        : null;
            }
            startDate = sub.getStartDate();
            endDate = sub.getEndDate();
        }

        // tổng dung lượng đã dùng từ album media
        Long usedStorageBytes = albumMediaRepo.sumFileSizeByFamilyId(familyId);
        Long usedStorageMb = usedStorageBytes != null ? usedStorageBytes / (1024 * 1024) : 0L;

        return DashboardRes.builder()
                .totalMembersInFamilyTree(getTotalMembersInFamilyTree(family))
                .totalEvents(familyEventRepo.countByFamily_FamilyId(familyId))
                .totalAlbumMedias(albumRepo.sumMediaCountByFamilyId(familyId))
                .totalAchievement(familyAchievementRepo.countByFamily_FamilyId(familyId))
                .totalAdmin(familyMemberRepo.countByFamily_FamilyIdAndRole_NameAndStatus(
                        familyId, "FAMILY_ADMIN", FamilyMemberStatus.ACTIVE))
                .totalStorages(maxStorageMb)
                .currentFamilyName(family.getFamilyName())
                .totalCustoms(ceremonyRepo.countByFamily_FamilyId(familyId))
                .totalUsedStorages(usedStorageMb)
                .currentSubscriptionPlanName(planName)
                .currentSubscriptionPlanPrice(planPrice)
                .currentSubscriptionStartDate(startDate)
                .currentSubscriptionEndDate(endDate)
                .build();
    }

    private Long getTotalMembersInFamilyTree(Family family) {
        List<FamilyCategory> categories = familyCategoryRepo.findAllByFamily_FamilyId(family.getFamilyId());
        return categories.stream()
                .mapToLong(c -> c.getTotalPerson() != null ? c.getTotalPerson() : 0L)
                .sum();
    }
}
