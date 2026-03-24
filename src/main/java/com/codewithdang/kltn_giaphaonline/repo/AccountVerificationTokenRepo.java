package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.AccountVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountVerificationTokenRepo extends JpaRepository<AccountVerificationToken, Long> {

    Optional<AccountVerificationToken> findByToken(String token);

    boolean existsByToken(String token);
}
