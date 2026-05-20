package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.ArticleContentFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleReq {
    @NotBlank(message = "Tiêu đề không được để trống")
    String title;

    String summary;
    String content;
    Boolean isFeatured;
    String metaTitle;
    String metaDescription;

    @NotNull(message = "Định dạng nội dung không được để trống")
    ArticleContentFormat contentFormat;

    Long articleCategoryId;
    MultipartFile thumbnail;
}
