package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ArticleCategoryRes {
    Long articleCategoryId;
    String name;
    String slug;
    String description;
    Integer displayOrder;
    Instant createdAt;
}
