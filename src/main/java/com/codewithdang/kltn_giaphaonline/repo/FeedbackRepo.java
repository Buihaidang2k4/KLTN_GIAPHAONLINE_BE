package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    Page<Feedback> findAllByAccount_AccountId(Long accountId, Pageable pageable);

    Page<Feedback> findAllByAccount_AccountIdAndSubjectContainingIgnoreCase(Long accountId, String subject, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE " +
            "(:keyword IS NULL OR LOWER(f.subject) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) " +
            "OR LOWER(f.account.email) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))")
    Page<Feedback> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
