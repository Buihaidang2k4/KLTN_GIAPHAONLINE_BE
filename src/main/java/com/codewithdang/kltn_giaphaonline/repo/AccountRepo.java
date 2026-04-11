package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
