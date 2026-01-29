package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import com.codewithdang.kltn_giaphaonline.entity.AccountRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepo extends JpaRepository<AccountRole, AccountRoleId> {
    boolean existsByRoleName(String roleName);

    long countByRole_Name(String roleName);

    boolean existsByRoleNameAndAccount_AccountId(String roleName, Long accountAccountId);

    AccountRole findByAccount_AccountIdAndRole_Name(Long accountAccountId, String roleName);
}
