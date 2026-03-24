package com.codewithdang.kltn_giaphaonline.entity;


import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    SubscriptionPlan subscriptionPlan;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    BigDecimal amount;

    @Column(name = "currency", length = 3)
    String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 20)
    PaymentProvider provider;

    @Column(name = "provider_transaction_id", unique = true)
    String providerTransactionId;

    @Column(name = "merchant_transaction_id", unique = true)
    String merchantTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    PaymentStatus status;

    @Column(name = "failure_reason", length = 500)
    String failureReason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_callback", columnDefinition = "jsonb")
    String rawCallback;

    @Column(name = "paid_at")
    Instant paidAt;

    @Column(name = "created_at")
    Instant createdAt;
}