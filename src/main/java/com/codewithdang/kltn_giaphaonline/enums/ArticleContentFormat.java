package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleContentFormat implements BaseEnum {
    HTML("html"),
    MARKDOWN("markdown"),
    ;

    private final String label;

    @Override
    public String getLabel() {
        return "";
    }
}
