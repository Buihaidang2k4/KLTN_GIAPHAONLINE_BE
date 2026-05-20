package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.ArticleContentFormat;
import com.codewithdang.kltn_giaphaonline.enums.ArticleStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleRes {
    Long articleId;
    String slug;
    String title;
    String summary;
    String thumbnailUrl;
    String content;
    Integer viewCount;
    Boolean isFeatured;
    String metaTitle;
    String metaDescription;
    ArticleContentFormat contentFormat;
    ArticleStatus status;
    Instant publishedAt;
    Instant createdAt;
    Instant updatedAt;
    Long articleCategoryId;
    String articleCategoryName;
    Long createdByAccountId;
    String createdByAccountName;
}
