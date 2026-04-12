package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimelinePreparation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeremonyTimelinePreparationRepo extends JpaRepository<CeremonyTimelinePreparation, Long> {
    Page<CeremonyTimelinePreparation> findAllByTimeline_TimelineId(Pageable pageable, Long timelineTimelineId);
}
