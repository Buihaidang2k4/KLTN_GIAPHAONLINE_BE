package com.codewithdang.kltn_giaphaonline.repo;


import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyRepo extends JpaRepository<Family, Long> {
    Optional<Family> findByFamilyIdAndOwner_AccountId(Long familyId, Long ownerAccountId);

    void deleteByOwner(Account owner);
}