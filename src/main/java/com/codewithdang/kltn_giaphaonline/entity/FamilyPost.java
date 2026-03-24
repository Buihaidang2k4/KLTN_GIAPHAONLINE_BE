package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.FamilyPostStatus;
import com.codewithdang.kltn_giaphaonline.enums.FamilyPostType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @Column(name = "title", length = 255)
    String title;

    @Lob
    @Column(name = "content")
    String content;

    @Column(name = "thumbnail_url", length = 500)
    String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", length = 20)
    FamilyPostType postType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    FamilyPostStatus status;

    @Column(name = "published_at")
    Instant publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id")
    Account createdByAccount;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "deleted_at")
    Instant deletedAt;
}