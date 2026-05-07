package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilySubscription;
import com.codewithdang.kltn_giaphaonline.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilySubscriptionRepo extends JpaRepository<FamilySubscription, Long> {
    Optional<FamilySubscription> findFirstByFamilyAndStatusOrderByCreatedAtDesc(Family family, SubscriptionStatus status);
}
