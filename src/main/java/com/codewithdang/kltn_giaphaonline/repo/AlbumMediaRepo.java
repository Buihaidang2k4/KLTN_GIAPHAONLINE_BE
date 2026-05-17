package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.AlbumMedia;
import com.codewithdang.kltn_giaphaonline.enums.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumMediaRepo extends JpaRepository<AlbumMedia, Long> {
    @Query("""
                SELECT m
                FROM AlbumMedia m
                WHERE m.album.albumId = :albumId
                AND (:mediaType IS NULL OR m.mediaType = :mediaType)
            """)
    Page<AlbumMedia> searchMedia(
            Long albumId,
            MediaType mediaType,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(m.fileSizeBytes), 0) FROM AlbumMedia m WHERE m.album.family.familyId = :familyId")
    Long sumFileSizeByFamilyId(Long familyId);
}
