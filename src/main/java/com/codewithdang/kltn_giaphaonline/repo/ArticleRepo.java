package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Article;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long> {

    boolean existsBySlug(String slug);

    Optional<Article> findBySlug(String slug);

    @Query("SELECT a FROM Article a WHERE a.deletedAt IS NULL " +
           "AND (:keyword IS NULL OR :keyword = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:categoryId IS NULL OR a.articleCategory.articleCategoryId = :categoryId)")
    Page<Article> search(@Param("keyword") String keyword,
                         @Param("status") ArticleStatus status,
                         @Param("categoryId") Long categoryId,
                         Pageable pageable);
}
