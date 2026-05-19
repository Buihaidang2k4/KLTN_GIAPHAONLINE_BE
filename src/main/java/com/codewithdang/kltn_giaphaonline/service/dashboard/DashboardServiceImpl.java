package com.codewithdang.kltn_giaphaonline.service.dashboard;

import com.codewithdang.kltn_giaphaonline.dto.response.DashboardRes;
import com.codewithdang.kltn_giaphaonline.dto.response.DashboardSystemRes;
import com.codewithdang.kltn_giaphaonline.dto.response.MonthlyStatRes;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.enums.FamilyMemberStatus;
import com.codewithdang.kltn_giaphaonline.enums.PaymentStatus;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    AccountRepo accountRepo;
    PaymentRepo paymentRepo;


    /***
     * user
     * @param familyId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public DashboardRes getDataDashboardUser(Long familyId) {
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

    @Override
    @Transactional(readOnly = true)
    public DashboardSystemRes getDataDashboardSystem() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twelveMonthsAgo = now.minusMonths(12).withDayOfMonth(1);
        Instant twelveMonthsAgoInstant = twelveMonthsAgo.atZone(ZoneId.systemDefault()).toInstant();

        int thisYear = now.getYear();
        int thisMonth = now.getMonthValue();
        int lastYear = now.minusMonths(1).getYear();
        int lastMonth = now.minusMonths(1).getMonthValue();

        // account
        long totalAccounts = accountRepo.count();
        long totalActive = accountRepo.countByAccountStatus(AccountStatus.ACTIVE);
        long totalLocked = accountRepo.countByAccountStatus(AccountStatus.LOCKED);

        // family
        long totalFamilies = familyRepo.count();
        long totalFamiliesWithSub = familySubscriptionRepo.countByStatus(SubscriptionStatus.ACTIVE);

        // subscription
        long totalActiveSub = familySubscriptionRepo.countByStatus(SubscriptionStatus.ACTIVE);
        long totalExpiredSub = familySubscriptionRepo.countByStatus(SubscriptionStatus.EXPIRED);

        // payment
        long totalPayments = paymentRepo.count();
        long totalSuccess = paymentRepo.countByStatus(PaymentStatus.SUCCESS);
        long totalFailed = paymentRepo.countByStatus(PaymentStatus.FAILED);
        BigDecimal revenueTotal = paymentRepo.sumTotalRevenue();
        BigDecimal revenueThisMonth = paymentRepo.sumRevenueByMonth(thisYear, thisMonth);
        BigDecimal revenueLastMonth = paymentRepo.sumRevenueByMonth(lastYear, lastMonth);

        // account growth 12 tháng
        List<MonthlyStatRes> accountGrowth = accountRepo
                .countNewAccountsGroupByMonth(twelveMonthsAgo)
                .stream()
                .map(row -> new MonthlyStatRes((String) row[0], ((Number) row[1]).longValue(), null))
                .toList();

        // revenue growth 12 tháng
        List<MonthlyStatRes> revenueGrowth = paymentRepo
                .sumRevenueGroupByMonth(twelveMonthsAgoInstant)
                .stream()
                .map(row -> new MonthlyStatRes((String) row[0], null, new BigDecimal(row[1].toString())))
                .toList();

        return DashboardSystemRes.builder()
                .totalAccounts(totalAccounts)
                .totalAccountsActive(totalActive)
                .totalAccountsLocked(totalLocked)
                .totalFamilies(totalFamilies)
                .totalFamiliesWithActiveSub(totalFamiliesWithSub)
                .totalActiveSubscriptions(totalActiveSub)
                .totalExpiredSubscriptions(totalExpiredSub)
                .totalPayments(totalPayments)
                .totalPaymentsSuccess(totalSuccess)
                .totalPaymentsFailed(totalFailed)
                .revenueTotal(revenueTotal)
                .revenueThisMonth(revenueThisMonth)
                .revenueLastMonth(revenueLastMonth)
                .accountGrowth(accountGrowth)
                .revenueGrowth(revenueGrowth)
                .build();
    }

    private Long getTotalMembersInFamilyTree(Family family) {
        List<FamilyCategory> categories = familyCategoryRepo.findAllByFamily_FamilyId(family.getFamilyId());
        return categories.stream()
                .mapToLong(c -> c.getTotalPerson() != null ? c.getTotalPerson() : 0L)
                .sum();
    }
}
