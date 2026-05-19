package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMerchantTransactionId(String merchantTransactionId);

    boolean existsByMerchantTransactionId(String merchantTransactionId);

    Page<Payment> findByFamily_FamilyId(Long familyFamilyId, Pageable pageable);

    long countByStatus(com.codewithdang.kltn_giaphaonline.enums.PaymentStatus status);

    long count();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = com.codewithdang.kltn_giaphaonline.enums.PaymentStatus.SUCCESS AND FUNCTION('date_part', 'year', p.paidAt) = :year AND FUNCTION('date_part', 'month', p.paidAt) = :month")
    BigDecimal sumRevenueByMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT TO_CHAR(paid_at, 'YYYY-MM') as month, COALESCE(SUM(amount), 0) as value FROM payments WHERE status = 'SUCCESS' AND paid_at >= :from GROUP BY TO_CHAR(paid_at, 'YYYY-MM') ORDER BY month ASC", nativeQuery = true)
    List<Object[]> sumRevenueGroupByMonth(@Param("from") java.time.Instant from);
}
