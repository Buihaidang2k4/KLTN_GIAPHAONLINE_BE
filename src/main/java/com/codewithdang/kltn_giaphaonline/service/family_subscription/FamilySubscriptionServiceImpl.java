package com.codewithdang.kltn_giaphaonline.service.family_subscription;

import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionCheckQuotaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionRes;
import com.codewithdang.kltn_giaphaonline.entity.*;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilySubscriptionMapper;
import com.codewithdang.kltn_giaphaonline.repo.AlbumRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilySubscriptionRepo;
import com.codewithdang.kltn_giaphaonline.service.subscription_plan.SubscriptionPlanServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FamilySubscriptionServiceImpl implements FamilySubscriptionService {
    FamilySubscriptionRepo familySubscriptionRepo;
    SubscriptionPlanServiceImpl planService;
    FamilyRepo familyRepo;
    FamilyCategoryRepo familyCategoryRepo;
    FamilySubscriptionMapper subscriptionMapper;
    AlbumRepo albumRepo;

    /***
     * active subscription khi payment thanh cong
     * @param payment
     * @return
     */
    @Override
    @Transactional
    public FamilySubscription activateSubscription(Payment payment) {
        SubscriptionPlan subscriptionPlan = payment.getSubscriptionPlan();
        LocalDate today = LocalDate.now();

        FamilySubscription familySubscription = familySubscriptionRepo
                .findFirstByFamilyAndStatusOrderByCreatedAtDesc(
                        payment.getFamily(),
                        SubscriptionStatus.ACTIVE
                )
                .orElse(null);

        // tạo mới nếu chưa dùng gói bao giờ
        if (familySubscription == null) {
            familySubscription = FamilySubscription.builder()
                    .createdByAccount(payment.getAccount())
                    .family(payment.getFamily())
                    .subscriptionPlan(subscriptionPlan)
                    .status(SubscriptionStatus.ACTIVE)
                    .autoRenewal(false)
                    .startDate(today)
                    .canceledAt(null)
                    .expiredAt(null)
                    .endDate(calculateEndDate(today, subscriptionPlan))
                    .build();

            return familySubscriptionRepo.save(familySubscription);
        }

        // gia hạn | nâng cấp gói
        familySubscription.setSubscriptionPlan(subscriptionPlan);
        familySubscription.setStatus(SubscriptionStatus.ACTIVE);
        familySubscription.setCanceledAt(null);
        familySubscription.setExpiredAt(null);

        LocalDate baseDate = familySubscription.getEndDate() != null && familySubscription.getEndDate().isAfter(today)
                ? familySubscription.getEndDate()
                : today;

        familySubscription.setEndDate(calculateEndDate(baseDate, subscriptionPlan));

        return familySubscriptionRepo.save(familySubscription);
    }

    /**
     * cancel subcription auto renewal
     *
     * @param familyId
     */
    @Transactional
    public void cancelAutoRenewal(Long familyId) {
        FamilySubscription subscription = familySubscriptionRepo
                .findFirstByFamily_FamilyIdOrderByCreatedAtDesc(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_SUBSCRIPTION_NOT_FOUND));

        subscription.setAutoRenewal(false);
        subscription.setCanceledAt(Instant.now());

        familySubscriptionRepo.save(subscription);
    }

    // job expire
    @Scheduled(cron = "0 0 0 * * *", zone = "${app.timezone}")
    @Transactional
    public void expireSubscriptions() {
        LocalDate today = LocalDate.now();

        List<FamilySubscription> subscriptions = familySubscriptionRepo.findByStatusAndEndDateBefore(
                SubscriptionStatus.ACTIVE, today
        );

        for (FamilySubscription sub : subscriptions) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            sub.setExpiredAt(Instant.now());
        }

        familySubscriptionRepo.saveAll(subscriptions);
    }

    private LocalDate calculateEndDate(LocalDate baseDate, SubscriptionPlan plan) {
        Integer durationMonth = plan.getDurationMonth();

        if (durationMonth == null || durationMonth <= 0) {
            return null;
        }

        return baseDate.plusMonths(durationMonth);
    }

    @Override
    @Transactional
    public FamilySubscription subscribeFamilyToDefaultPlan(Long familyId, Account account) {
        log.info("Subscribing family {} to default (FREE) plan", familyId);
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        // Get or create default FREE plan
        SubscriptionPlan defaultPlan = planService.getOrCreateDefaultPlan();

        LocalDate today = LocalDate.now();
        FamilySubscription familySubscription = FamilySubscription.builder()
                .createdByAccount(account)
                .family(family)
                .subscriptionPlan(defaultPlan)
                .status(SubscriptionStatus.ACTIVE)
                .autoRenewal(true)
                .startDate(today)
                .canceledAt(null)
                .expiredAt(null)
                .endDate(calculateEndDate(today, defaultPlan))
                .build();

        familySubscriptionRepo.save(familySubscription);
        log.info("Family {} subscribed to default FREE plan. Expiry date: {}",
                family.getFamilyId(), familySubscription.getEndDate());
        return familySubscription;
    }

    @Override
    @Transactional(readOnly = true)
    public FamilySubscriptionRes getByFamilyId(Long familyId) {
        return familySubscriptionRepo.findByFamily_FamilyId(familyId).map(subscriptionMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_SUBSCRIPTION_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public FamilySubscriptionCheckQuotaRes getFamilySubByQuotaUsage(Long familyId) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Long currentAdminCount = 0L;
        Long currentPersonCount = 0L;
        Long currentStorageUsedMb = 0L;

        currentAdminCount = family.getMembers().stream()
                .map(member -> member.getRole().getName().equals(RoleEnums.FAMILY_ADMIN.name()))
                .count();

        currentPersonCount = familyCategoryRepo.findAllByFamily(family).stream()
                .map(category -> category.getTotalPerson())
                .reduce(0L, Long::sum);

        currentStorageUsedMb = albumRepo.getAllByFamily(family)
                .stream()
                .map(album -> album.getTotalSize())
                .reduce(0L, Long::sum) / (1024 * 1024);

        return FamilySubscriptionCheckQuotaRes.builder()
                .currentAdminCount(currentAdminCount)
                .currentPersonCount(currentPersonCount)
                .currentStorageUsedMb(currentStorageUsedMb)
                .build();

    }
}
