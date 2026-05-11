package com.codewithdang.kltn_giaphaonline.service.subscription;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Subscription;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;

public interface SubscriptionService {
    /**
     * Subscribe account to a subscription plan (for new account)
     */
    Subscription subscribeAccountToDefaultPlan(Account account);

    /**
     * Subscribe account to specific plan
     */
    Subscription subscribeToPlan(Account account, SubscriptionPlan plan, boolean autoRenew);

    /**
     * Get current active subscription for account (if exists)
     */
    Subscription getCurrentSubscription(Long accountId);

    /**
     * Cancel active subscription
     */
    void cancelSubscription(Long subscriptionId, String reason);
}

