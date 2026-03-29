package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.ArticleContentFormat;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "articles",
        indexes = {
                @Index(name = "idx_article_slug", columnList = "slug"),
                @Index(name = "idx_article_status", columnList = "status"),
                @Index(name = "idx_article_published_at", columnList = "published_at"),
        }
)
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
    @Column(name = "content", columnDefinition = "TEXT")
    String content;

    @Builder.Default
    @Column(name = "view_count")
    Integer viewCount = 0;

    @Builder.Default
    @Column(name = "is_featured")
    Boolean isFeatured = false;

    @Column(name = "meta_title", length = 255)
    String metaTitle;

    @Column(name = "meta_description", length = 500)
    String metaDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_format", nullable = false, length = 20)
    ArticleContentFormat contentFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    ArticleStatus status;

    @Column(name = "published_at")
    Instant publishedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
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
}