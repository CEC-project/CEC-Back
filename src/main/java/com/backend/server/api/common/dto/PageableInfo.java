package com.backend.server.api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class PageableInfo {
    @Schema(description = "현재 페이지 번호", example = "1")
    private int pageNumber;

    @Schema(description = "페이지 당 아이템 수", example = "1")
    private int pageSize;

    @Schema(description = "전체 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "전체 아이템 수", example = "1")
    private long totalElements;

    public PageableInfo(Page<?> page) {
        Pageable pageable = page.getPageable();
        pageNumber = pageable.getPageNumber();
        pageSize = page.getNumberOfElements();
        totalPages = page.getTotalPages();
        totalElements = page.getTotalElements();
    }
}
