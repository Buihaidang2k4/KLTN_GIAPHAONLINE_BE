package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum String implements BaseEnum {
    IMAGE("Hình ảnh"),    // .jpg, .png, .gif
    VIDEO("Video"),       // .mp4, .mov
    DOCUMENT("Tài liệu"),  // .pdf, .docx, .txt
    OTHER("Khác");        // Các định dạng lạ khác

    private final java.lang.String label;

    @Override
    public java.lang.String getLabel() {
        return this.label;
    }
}