package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountVerificationToken;
import org.mapstruct.MappingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountVerificationTokenRepo extends JpaRepository<AccountVerificationToken, Long> {

    Optional<AccountVerificationToken> findByToken(String token);

    @Modifying
    @Query("""
                    update AccountVerificationToken t
                    set  t.isUsed = true 
                    where t.isUsed = false and t.account = :account
            """)
    void updateVerificationTokenOldAndIsUsedTrueByAccount(@Param("account") Account account);

    void deleteByAccount(Account account);
}
