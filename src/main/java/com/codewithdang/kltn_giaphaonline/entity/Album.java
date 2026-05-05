package com.codewithdang.kltn_giaphaonline.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "albums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    Long albumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id", nullable = false)
    Account createdByAccount;

    @Column(name = "title", length = 255, nullable = false)
    String title;

    @Column(name = "slug", length = 255)
    String slug;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "cover_path", length = 500)
    String coverPath;

    @Builder.Default
    @Column(name = "total_size", nullable = false)
    Long totalSize = 0L;

    @Builder.Default
    @Column(name = "media_count", nullable = false)
    Integer mediaCount = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

}