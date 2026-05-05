package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRes {
    Long albumId;
    Long familyId;
    Long createdByAccountId;
    String title;
    String slug;
    String description;
    String coverPath;
    String coverUrl;
    Long totalSize;
    Integer mediaCount;
    Instant createdAt;
    Instant updatedAt;
}
