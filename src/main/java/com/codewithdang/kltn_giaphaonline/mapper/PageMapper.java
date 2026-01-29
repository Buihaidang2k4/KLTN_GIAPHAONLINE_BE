package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <E, D> PageResponse<D> toPageResponse(
            Page<E> page,
            Function<E, D> mapper
    ) {
        List<D> items = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return PageResponse.<D>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .sortBy(page.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse(null))
                .sortDirection(page.getSort().stream().findFirst().map(o -> o.getDirection().name()).orElse(null))
                .items(items)
                .build();
    }
}

