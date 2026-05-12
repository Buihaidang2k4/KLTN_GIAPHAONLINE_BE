package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyCategoryRepo extends JpaRepository<FamilyCategory, Long> {
    Page<FamilyCategory> findAllByFamily_FamilyId(Long familyFamilyId, Pageable pageable);
}
