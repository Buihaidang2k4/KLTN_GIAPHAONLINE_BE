package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaType implements BaseEnum {
    IMAGE("Hình ảnh"),    // .jpg, .png, .gif
    VIDEO("Video"),       // .mp4, .mov
    DOCUMENT("Tài liệu"),  // .pdf, .docx, .txt
    LINK("Đường dẫn đến tài liệu/video/img"),
    OTHER("Khác");        // Các định dạng lạ khác

    private final java.lang.String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}