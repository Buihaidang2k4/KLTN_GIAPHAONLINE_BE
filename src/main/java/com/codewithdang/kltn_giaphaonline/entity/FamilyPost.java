package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.FamilyPostStatus;
import com.codewithdang.kltn_giaphaonline.enums.FamilyPostType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "family_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_post_id")
    Long familyPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    FamilyPostCategory category;

    @Column(name = "title", length = 255, nullable = false)
    String title;

    @Lob
    @Column(name = "content")
    String content;

    @Column(name = "thumbnail_path", length = 500)
    String thumbnailPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", length = 20)
    FamilyPostType postType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    FamilyPostStatus status;

    @Column(name = "published_at")
    Instant publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id", nullable = false)
    Account createdByAccount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "deleted_at")
    Instant deletedAt;
}