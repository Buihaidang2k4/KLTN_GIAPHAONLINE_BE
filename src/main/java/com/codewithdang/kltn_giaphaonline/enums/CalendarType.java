package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CalendarType implements BaseEnum {
    SOLAR("Dương lịch"),
    LUNAR("Âm lịch");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}