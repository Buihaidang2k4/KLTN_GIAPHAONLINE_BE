package com.codewithdang.kltn_giaphaonline.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPlanReq {

    @NotNull(message = "Tên gói không được để trống")
    String namePlan;

    @NotBlank(message = "Mã gói không được để trống")
    @Size(max = 50, message = "Mã gói không được vượt quá 50 ký tự")
    String code;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    String description;

    @NotNull(message = "Giá gói không được để trống")
    @DecimalMin(value = "0.00", message = "Giá gói không được âm")
    BigDecimal price;

    @NotBlank(message = "Đơn vị tiền tệ không được để trống")
    @Size(min = 3, max = 3, message = "Currency phải có 3 ký tự, ví dụ VND")
    String currency;

    @NotNull(message = "Số thành viên tối đa không được để trống")
    @Min(value = 1, message = "Số thành viên tối đa phải lớn hơn 0")
    Integer maxPerson;

    @NotNull(message = "Dung lượng tối đa không được để trống")
    @Min(value = 0, message = "Dung lượng không được âm")
    Integer maxStorageMb;

    @NotNull(message = "Thời hạn gói không được để trống")
    @Min(value = 0, message = "Thời hạn không được âm")
    Integer durationMonth;

    Boolean isActive;
}