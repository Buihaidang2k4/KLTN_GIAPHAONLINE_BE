package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackReq {
    @NotBlank(message = "Vui lòng nhập tiêu đề")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    String subject;

    @NotBlank(message = "Vui lòng nhập nội dung")
    @Size(max = 1000, message = "Nội dung không được vượt quá 1000 ký tự")
    String content;

    @NotNull(message = "Vui lòng chọn loại phản hồi")
    FeedbackType type;
}
