package com.codewithdang.kltn_giaphaonline.service.family_subscription;

import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionCheckQuotaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilySubscriptionRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.entity.Payment;

public interface FamilySubscriptionService {
    FamilySubscription activateSubscription(Payment payment);

    public void cancelAutoRenewal(Long familyId);

    public void expireSubscriptions();

    FamilySubscription subscribeFamilyToDefaultPlan(Long familyId, Account account);

    FamilySubscriptionRes getByFamilyId(Long familyId);

    FamilySubscriptionCheckQuotaRes getFamilySubByQuotaUsage(Long familyId);
}
