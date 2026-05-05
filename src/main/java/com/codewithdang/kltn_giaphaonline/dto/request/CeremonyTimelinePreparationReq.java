package com.codewithdang.kltn_giaphaonline.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CeremonyTimelinePreparationReq {

    @NotBlank(message = "Tên vật dụng không được để trống")
    @Size(max = 255, message = "Tên vật dụng không vượt quá 255 ký tự")
    String itemName;

    @NotBlank(message = "Loại vật dụng không được để trống")
    String itemType;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng tối thiểu phải là 1")
    Integer quantity;

    @NotBlank(message = "Đơn vị tính không được để trống")
    String unit;

    @Size(max = 500, message = "Ghi chú không vượt quá 500 ký tự")
    String note;

    @NotNull(message = "Trạng thái bắt buộc không được để trống")
    Boolean required;
}