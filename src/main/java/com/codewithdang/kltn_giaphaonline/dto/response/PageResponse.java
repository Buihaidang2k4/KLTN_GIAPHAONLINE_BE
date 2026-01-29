package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageResponse<T> {
    private int page;
    private int size;

    private long totalElements;
    private int totalPages;

    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    private String sortBy;
    private String sortDirection;

    private List<T> items;
}
