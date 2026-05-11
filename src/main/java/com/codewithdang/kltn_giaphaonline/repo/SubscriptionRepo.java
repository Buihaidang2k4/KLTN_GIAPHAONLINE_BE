package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Subscription;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

    /**
     * Find current active subscription for account
     */
    @Query("""
            SELECT s FROM Subscription s
            WHERE s.account.accountId = :accountId
            AND s.status = 'ACTIVE'
            ORDER BY s.createdAt DESC
            LIMIT 1
            """)
    Optional<Subscription> findCurrentActiveSubscription(@Param("accountId") Long accountId);

    /**
     * Find latest subscription for account (active or expired)
     */
    @Query("""
            SELECT s FROM Subscription s
            WHERE s.account.accountId = :accountId
            ORDER BY s.createdAt DESC
            LIMIT 1
            """)
    Optional<Subscription> findLatestSubscription(@Param("accountId") Long accountId);

    /**
     * Check if account has active subscription
     */
    boolean existsByAccountAndStatus(Account account, SubscriptionStatus status);
}

