package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.FamilyPostCategory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyPostCategoryRepo extends JpaRepository<FamilyPostCategory, Long> {
    Page<FamilyPostCategory> findAllByFamily_FamilyId(Long familyFamilyId, Pageable pageable);

    Optional<FamilyPostCategory> findByFamily_FamilyIdAndCategoryId(Long familyFamilyId, Long categoryId);

    boolean existsByFamily_FamilyIdAndName(Long familyId, @NotNull String name);
}
