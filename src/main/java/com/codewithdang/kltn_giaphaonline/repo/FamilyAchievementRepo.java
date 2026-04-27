package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.FamilyAchievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyAchievementRepo extends JpaRepository<FamilyAchievement, Long> {
    Page<FamilyAchievement> findAllByFamily_FamilyId(Long familyId, Pageable pageable);
}
