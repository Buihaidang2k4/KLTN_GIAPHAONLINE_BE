package com.codewithdang.kltn_giaphaonline.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPlanRes {

    Long subscriptionPlanId;
    String namePlan;
    String code;
    String description;
    BigDecimal price;
    String currency;
    Integer maxPerson;
    Integer maxStorageMb;
    Integer durationMonth;
    Boolean isActive;
    Instant createdAt;
    Instant updatedAt;
}