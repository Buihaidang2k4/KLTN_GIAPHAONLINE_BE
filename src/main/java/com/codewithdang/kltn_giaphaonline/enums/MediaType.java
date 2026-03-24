package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaType implements BaseEnum {
    IMAGE("Hình ảnh"),    // .jpg, .png, .gif
    VIDEO("Video"),       // .mp4, .mov
    AUDIO("Âm thanh"),    // .mp3, .wav (Gợi ý thêm: dùng cho lời kể, băng ghi âm cũ)
    DOCUMENT("Tài liệu"),  // .pdf, .docx, .txt
    OTHER("Khác");        // Các định dạng lạ khác

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}