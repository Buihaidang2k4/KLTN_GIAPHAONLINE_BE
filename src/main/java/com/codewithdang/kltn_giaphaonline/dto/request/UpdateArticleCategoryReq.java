package com.codewithdang.kltn_giaphaonline.dto.request;


import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateArticleCategoryReq {

    @Size(max = 255, message = "CATEGORY_NAME_TOO_LONG")
    String name;
    String description;
    Integer displayOrder;
}