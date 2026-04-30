package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyEventRepo extends JpaRepository<FamilyEvent, Long> {
    Optional<FamilyEvent> findByFamily_FamilyIdAndFamilyEventId(@Param("familyFamilyId") Long familyFamilyId,
                                                                @Param("familyEventId") Long familyEventId);

    @Query("SELECT e FROM FamilyEvent e WHERE e.family.familyId = :familyId AND (:keyword = '' OR :keyword IS NULL OR LOWER(e.eventName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<FamilyEvent> findAllByFamily_FamilyIdAndKeyword(@Param("familyId") Long familyId, @Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT e FROM FamilyEvent e
            WHERE e.family.familyId = :familyId
            AND (:keyword IS NULL OR :keyword = '' OR LOWER(e.eventName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND e.nextOccurrenceDate IS NOT NULL
            AND e.nextOccurrenceDate BETWEEN :fromDate AND :toDate
            ORDER BY e.nextOccurrenceDate ASC
            """)
    Page<FamilyEvent> findUpcomingEvents(
            @Param("familyId") Long familyId,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );

    // find events whose next occurrence is on or before given date
    List<FamilyEvent> findAllByNextOccurrenceDateLessThanEqual(LocalDate date);
}
