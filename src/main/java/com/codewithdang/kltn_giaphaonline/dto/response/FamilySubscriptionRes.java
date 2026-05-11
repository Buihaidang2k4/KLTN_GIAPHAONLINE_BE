package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    SubscriptionStatus status;
    Boolean autoRenewal;
    Instant createdAt;
    Instant updatedAt;
    LocalDate startDate;
    LocalDate endDate;
    Instant canceledAt;
    Instant expiredAt;
}
