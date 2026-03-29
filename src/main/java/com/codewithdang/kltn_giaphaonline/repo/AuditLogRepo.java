package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByFamilyIdOrderByCreatedAtDesc(Long familyId, Pageable pageable);

    Page<AuditLog> findByActor_AccountIdOrderByCreatedAtDesc(Long actor_accountId, Pageable pageable);
}
