package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CeremonyTimelinePreparationUpdateReq {

    @NotBlank(message = "Tên vật dụng không được để trống")
    @Size(max = 255, message = "Tên vật dụng không quá 255 ký tự")
    String itemName;

    @NotBlank(message = "Loại vật dụng không được để trống")
    String itemType;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    Integer quantity;

    @NotBlank(message = "Đơn vị tính không được để trống")
    String unit;

    @Size(max = 500, message = "Ghi chú không quá 500 ký tự")
    String note;

    @NotNull(message = "Trạng thái bắt buộc không được để trống")
    Boolean required;
}