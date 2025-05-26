package com.backend.server.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class PageableInfo {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    public PageableInfo(Page<?> page) {
        Pageable pageable = page.getPageable();
        pageNumber = pageable.getPageNumber();
        pageSize = pageable.getPageSize();
        totalPages = page.getTotalPages();
        totalElements = page.getTotalElements();
    }
}
