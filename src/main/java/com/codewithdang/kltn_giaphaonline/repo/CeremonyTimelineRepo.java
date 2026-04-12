package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimeline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CeremonyTimelineRepo extends JpaRepository<CeremonyTimeline, Long> {
    Page<CeremonyTimeline> findAllByCeremony_CeremonyId(Pageable pageable, Long ceremonyId);

    @Query("SELECT MAX(t.stepOrder) FROM CeremonyTimeline t WHERE t.ceremony.ceremonyId = :ceremonyId")
    Integer findMaxStepOrderByCeremonyId(@Param("ceremonyId") Long ceremonyId);

    @Modifying
    @Query("UPDATE CeremonyTimeline t SET t.stepOrder = t.stepOrder - 1 " +
            "WHERE t.ceremony.ceremonyId = :ceremonyId AND t.stepOrder > :deletedOrder")
    void shiftOrdersDown(@Param("ceremonyId") Long ceremonyId, @Param("deletedOrder") int deletedOrder);
}
