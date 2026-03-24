package com.codewithdang.kltn_giaphaonline.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    @JoinColumn(name = "family_id")
    Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id")
    Account createdByAccount;

    @Column(name = "title", length = 255)
    String title;

    @Column(name = "slug", length = 255)
    String slug;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "cover_url", length = 500)
    String coverUrl;

    @Column(name = "total_size")
    Long totalSize;

    @Column(name = "media_count")
    Integer mediaCount;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "deleted_at")
    Instant deletedAt;
}