package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.enums.PaymentStatus;
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
public class PaymentRes {
    Long paymentId;
    Long accountId;
    Long subscriptionPlanId;
    Long familyId;
    Long familySubscriptionId;
    BigDecimal amount;
    String currency;
    PaymentProvider provider;
    String providerTransactionId;
    String merchantTransactionId;
    PaymentStatus status;
    String bankCode;
    String bankTransactionNo;
    String failureReason;
    Instant paidAt;
    Instant createdAt;
    Instant updatedAt;
}
