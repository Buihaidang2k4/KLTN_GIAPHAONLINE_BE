package com.codewithdang.kltn_giaphaonline.service.payment;

import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PaymentRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.Payment;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.enums.PaymentProvider;
import com.codewithdang.kltn_giaphaonline.enums.PaymentStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PaymentMapper;
import com.codewithdang.kltn_giaphaonline.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    PaymentRepo paymentRepo;
    PageMapper pageMapper;
    PaymentMapper paymentMapper;

    /***
     * used when create new payment
     * for subscription
     * payment
     * @param account
     * @param subscriptionPlan
     * @param family
     * @return
     */
    @Override
    public Payment createPayment(Account account, SubscriptionPlan subscriptionPlan, Family family, PaymentProvider provider) {
        String txnRef = generateTxnRef(account.getAccountId());

        while (paymentRepo.existsByMerchantTransactionId(txnRef)) {
            txnRef = generateTxnRef(account.getAccountId());
        }

        Payment payment = Payment.builder()
                .account(account)
                .family(family)
                .merchantTransactionId(txnRef)
                .subscriptionPlan(subscriptionPlan)
                .amount(subscriptionPlan.getPrice())
                .currency(subscriptionPlan.getCurrency())
                .provider(provider)
                .status(PaymentStatus.PENDING)
                .build();

        return paymentRepo.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PaymentRes> getAll(Pageable pageable) {
        Page<Payment> payments = paymentRepo.findAll(pageable);
        return pageMapper.toPageResponse(payments, paymentMapper::toRes);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PaymentRes> getAllByFamilyId(Long familyId, Pageable pageable) {
        Page<Payment> payments = paymentRepo.findByFamily_FamilyId(familyId, pageable);
        return pageMapper.toPageResponse(payments, paymentMapper::toRes);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentRes getByTransactionId(String transactionId) {
        Payment payment = paymentRepo.findByMerchantTransactionId(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toRes(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        paymentRepo.delete(payment);
    }

    private String generateTxnRef(Long accountId) {
        return "SUB_" + accountId + "_" + System.currentTimeMillis();
    }
}
