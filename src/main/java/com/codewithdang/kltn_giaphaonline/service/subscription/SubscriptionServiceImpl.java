package com.codewithdang.kltn_giaphaonline.service.subscription;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Subscription;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.SubscriptionRepo;
import com.codewithdang.kltn_giaphaonline.service.subscription_plan.SubscriptionPlanServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionServiceImpl implements SubscriptionService {

    SubscriptionRepo subscriptionRepo;
    SubscriptionPlanServiceImpl planService;

    @Override
    @Transactional
    public Subscription subscribeAccountToDefaultPlan(Account account) {
        log.info("Subscribing account {} to default (FREE) plan", account.getAccountId());

        // Get or create default FREE plan
        SubscriptionPlan defaultPlan = planService.getOrCreateDefaultPlan();

        // Subscribe account to plan with auto-renew enabled for FREE plan
        return subscribeToPlan(account, defaultPlan, true);
    }

    @Override
    @Transactional
    public Subscription subscribeToPlan(Account account, SubscriptionPlan plan, boolean autoRenew) {
        log.info("Subscribing account {} to plan {}", account.getAccountId(), plan.getCode());

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(plan.getDurationMonth());

        Subscription subscription = Subscription.builder()
                .account(account)
                .subscriptionPlan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(today)
                .endDate(endDate)
                .autoRenew(autoRenew)
                .build();

        subscriptionRepo.save(subscription);
        log.info("Subscription created: account={}, plan={}, endDate={}",
                account.getAccountId(), plan.getCode(), endDate);

        return subscription;
    }

    @Override
    @Transactional(readOnly = true)
    public Subscription getCurrentSubscription(Long accountId) {
        return subscriptionRepo.findCurrentActiveSubscription(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void cancelSubscription(Long subscriptionId, String reason) {
        Subscription subscription = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelledAt(Instant.now());
        subscription.setCancellationReason(reason);
        subscriptionRepo.save(subscription);

        log.info("Subscription {} cancelled. Reason: {}", subscriptionId, reason);
    }
}

