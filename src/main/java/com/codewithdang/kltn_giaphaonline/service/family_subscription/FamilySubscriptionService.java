package com.codewithdang.kltn_giaphaonline.service.family_subscription;

import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.entity.Payment;

public interface FamilySubscriptionService {
    FamilySubscription activateSubscription(Payment payment);

    public void cancelAutoRenewal(Long familyId);

    public void expireSubscriptions();
}
