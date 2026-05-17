package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilySubscriptionRes {
    Long subscriptionPlanId;
    Long familyId;
    Long createdByAccountId;
    String planName;
    String planCode;
    BigDecimal price;
    Integer maxPerson;
    Integer maxAdmin;
    Integer maxStorageMb;
    Integer durationMonth;
    Boolean autoRenewal;
    Instant createdAt;
    Instant updatedAt;
    LocalDate startDate;
    LocalDate endDate;
    Instant canceledAt;
    Instant expiredAt;
}
