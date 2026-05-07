package com.codewithdang.kltn_giaphaonline.service.payment;

import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.Payment;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Payment createPayment(
            Account account,
            SubscriptionPlan subscriptionPlan,
            Family family,
            PaymentProvider provider
    );

    PageResponse<PaymentRes> getAll(Pageable pageable);
}
