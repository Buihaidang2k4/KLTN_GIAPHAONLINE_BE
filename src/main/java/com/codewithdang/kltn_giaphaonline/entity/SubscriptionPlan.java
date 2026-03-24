package com.codewithdang.kltn_giaphaonline.entity;


import com.codewithdang.kltn_giaphaonline.enums.PlanName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_plan_id")
    Long subscriptionPlanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name_plan", length = 30)
    PlanName namePlan;

    @Column(name = "code")
    String code;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "price", precision = 12, scale = 2)
    BigDecimal price;

    @Column(name = "currency", length = 3)
    String currency;

    @Column(name = "max_person")
    Integer maxPerson;

    @Column(name = "max_storage_mb")
    Integer maxStorageMb;

    @Column(name = "duration_month")
    Integer durationMonth;

    @Column(name = "is_active")
    Boolean isActive;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;
}