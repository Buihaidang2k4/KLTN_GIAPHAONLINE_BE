package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    long countByAccountStatus(AccountStatus status);

    @Query(value = "SELECT TO_CHAR(created_at, 'YYYY-MM') as month, COUNT(*) FROM accounts WHERE created_at >= :from GROUP BY TO_CHAR(created_at, 'YYYY-MM') ORDER BY month ASC", nativeQuery = true)
    List<Object[]> countNewAccountsGroupByMonth(@Param("from") java.time.LocalDateTime from);
}
