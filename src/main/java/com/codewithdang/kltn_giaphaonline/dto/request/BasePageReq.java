package com.codewithdang.kltn_giaphaonline.dto.request;

public record BasePageReq(
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    public BasePageReq {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0 || size > 100) size = 20;
        if (sortDirection == null) sortDirection = "ASC";
    }
}
