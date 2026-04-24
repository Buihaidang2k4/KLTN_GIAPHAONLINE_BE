package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.FamilyEvent;
import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FamilyEventRepo extends JpaRepository<FamilyEvent, Long> {
    Page<FamilyEvent> findAllByFamily_FamilyId(Long familyFamilyId, Pageable pageable);

    Optional<FamilyEvent> findByFamily_FamilyIdAndFamilyEventId(@Param("familyFamilyId") Long familyFamilyId,
                                                                @Param("familyEventId") Long familyEventId);

    @Query("""
            SELECT e FROM FamilyEvent e
            WHERE e.family.familyId = :familyId
            AND (:keyword IS NULL OR e.eventName LIKE %:keyword%)
            AND (:calendarType IS NULL OR e.calendarType = :calendarType)
            AND (:reminderType IS NULL OR e.reminderType = :reminderType)
            AND (
                (:startDate IS NULL OR :endDate IS NULL)
                OR
                (
                    e.solarDate BETWEEN :startDate AND :endDate
                    OR e.lunarDate BETWEEN :startDate AND :endDate
                )
            )
            """)
    Page<FamilyEvent> searchEvents(
            @Param("familyId") Long familyId,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("calendarType") CalendarType calendarType,
            @Param("reminderType") ReminderEventType reminderType,
            Pageable pageable
    );
}
