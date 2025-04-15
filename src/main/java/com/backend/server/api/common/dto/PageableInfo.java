package com.backend.server.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageableInfo {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
