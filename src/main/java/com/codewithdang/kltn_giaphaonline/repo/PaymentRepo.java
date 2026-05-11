package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMerchantTransactionId(String merchantTransactionId);

    boolean existsByMerchantTransactionId(String merchantTransactionId);

    Page<Payment> findByFamily_FamilyId(Long familyFamilyId, Pageable pageable);
}
