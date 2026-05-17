package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Ceremony;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CeremonyRepo extends JpaRepository<Ceremony, Long> {
    Page<Ceremony> findAllByFamily(Pageable pageable, Family family);

    @Query("""
            SELECT c FROM Ceremony c 
                        WHERE c.family.familyId = :familyId 
                       AND (:keyword = '' OR :keyword IS NULL OR LOWER(c.ceremonyName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Ceremony> findAllByFamilyAndKeyword(Long familyId, String keyword, Pageable pageable);

    Long countByFamily_FamilyId(Long familyFamilyId);
}
