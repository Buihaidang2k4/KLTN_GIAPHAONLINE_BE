package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleCategoryRepo extends JpaRepository<ArticleCategory, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndArticleCategoryIdNot(String name, Long articleCategoryId);

    boolean existsBySlugAndArticleCategoryIdNot(String slug, Long articleCategoryId);

    @Query("SELECT c FROM ArticleCategory c WHERE :keyword IS NULL OR :keyword = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ArticleCategory> searchByName(@Param("keyword") String keyword, Pageable pageable);
}
