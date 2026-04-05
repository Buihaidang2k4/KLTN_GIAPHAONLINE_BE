package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.CeremonyTimelinePreparation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeremonyTimelinePreparationRepo extends JpaRepository<CeremonyTimelinePreparation, Long> {
}
