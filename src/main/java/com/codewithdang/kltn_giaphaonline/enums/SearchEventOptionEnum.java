package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchEventOptionEnum implements BaseEnum {
    ALL("Tất cả"),
    UPCOMING("Sắp diễn ra trong 30 ngày"),
    ;

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
