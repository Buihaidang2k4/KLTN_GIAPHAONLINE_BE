package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    Page<Notification> findByRecipient_AccountIdOrderByCreatedAtDesc(Long recipientAccountId, Pageable pageable);

    Page<Notification> findByRecipient_AccountIdAndIsReadOrderByCreatedAtDesc(
            Long recipientAccountId,
            Boolean isRead,
            Pageable pageable
    );

    long countByRecipient_AccountIdAndIsRead(Long recipientAccountId, Boolean isRead);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :now " +
            "WHERE n.recipient.accountId = :accountId AND n.isRead = false")
    void markAllAsRead(@Param("accountId") Long accountId, @Param("now") Instant now);

    void deleteByRecipient(Account recipient);

    void deleteBySender(Account sender);
}
