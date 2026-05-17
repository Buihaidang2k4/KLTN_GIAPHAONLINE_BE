package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Album;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepo extends JpaRepository<Album, Long> {
    Page<Album> findAlbumsByFamily_FamilyIdAndTitleContainingIgnoreCase(Long familyId, String keyword, Pageable pageable);

    long countByFamily_FamilyId(Long familyId);

    @Query("""
                SELECT COALESCE(SUM(a.mediaCount),0)
                FROM Album a
                WHERE a.family.familyId = :familyId
            """)
    Long sumMediaCountByFamilyId(Long familyId);

    List<Album> getAllByFamily(Family family);
}
