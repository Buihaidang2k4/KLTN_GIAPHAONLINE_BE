package com.codewithdang.kltn_giaphaonline.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "name_plan", length = 30)
    String namePlan;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    String code;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    BigDecimal price;

    @Column(name = "currency", length = 3, nullable = false)
    String currency;

    @Column(name = "max_person")
    Integer maxPerson;

    @Column(name = "max_admin")
    Integer maxAdmin;

    @Column(name = "max_storage_mb")
    Integer maxStorageMb;

    @Column(name = "duration_month", nullable = false)
    Integer durationMonth;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}