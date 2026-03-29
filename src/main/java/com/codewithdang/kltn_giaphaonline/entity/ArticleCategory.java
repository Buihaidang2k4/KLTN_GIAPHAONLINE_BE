package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_category_id")
    Long articleCategoryId;

    @Column(name = "name", unique = true)
    String name;

    @Column(name = "slug", unique = true, length = 150)
    String slug;

    @Column(name = "display_order")
    Integer displayOrder;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "articleCategory")
    List<Article> articles = new ArrayList<>();
}