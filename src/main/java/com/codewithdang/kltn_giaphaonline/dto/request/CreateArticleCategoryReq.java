package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateArticleCategoryReq(
        @NotBlank(message = "Tên danh mục không được để trống")
        @Size(min = 2, max = 100, message = "Tên danh mục phải từ 2 đến 100 ký tự")
        String name,

        String description,

        @NotNull(message = "Thứ tự hiển thị không được để trống")
        @Min(value = 0, message = "Thứ tự hiển thị không được là số âm")
        Integer displayOrder
) {
}