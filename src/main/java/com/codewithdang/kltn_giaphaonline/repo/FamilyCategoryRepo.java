package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyCategoryRepo extends JpaRepository<FamilyCategory, Long> {
    Page<FamilyCategory> findAllByFamily_FamilyId(Long familyFamilyId, Pageable pageable);

    List<FamilyCategory> findAllByFamily_FamilyId(Long familyFamilyId);

    List<FamilyCategory> findAllByFamily(Family family);
}
