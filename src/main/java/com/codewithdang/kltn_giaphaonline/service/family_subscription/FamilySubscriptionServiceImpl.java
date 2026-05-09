package com.codewithdang.kltn_giaphaonline.service.family_subscription;

import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.entity.Payment;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.FamilySubscriptionRepo;
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
}
