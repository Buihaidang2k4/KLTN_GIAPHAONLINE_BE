package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleCategoryRepo extends JpaRepository<ArticleCategory, Long> {
    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    Optional<ArticleCategory> findBySlug(String slug);

    boolean existsByNameAndArticleCategoryIdNot(String name, Long articleCategoryId);

    boolean existsBySlugAndArticleCategoryIdNot(String slug, Long articleCategoryId);
}
