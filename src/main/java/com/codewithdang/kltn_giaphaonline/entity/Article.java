package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    Long articleId;

    @Column(name = "slug", unique = true, length = 255)
    String slug;

    @Column(name = "title")
    String title;

    @Column(name = "summary", length = 500)
    String summary;

    @Column(name = "thumbnail_url", length = 500)
    String thumbnailUrl;

    @Lob
    @Column(name = "content")
    String content;

    @Column(name = "view_count")
    Integer viewCount;

    @Column(name = "is_featured")
    Boolean isFeatured;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    ArticleStatus status;

    @Column(name = "published_at")
    Instant publishedAt;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "deleted_at")
    Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_category_id")
    ArticleCategory articleCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id")
    Account createdByAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by_account_id")
    Account updatedByAccount;
}