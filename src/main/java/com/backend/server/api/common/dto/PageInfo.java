package com.backend.server.api.common.dto;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private int totalElements;
    private int size;
    private int totalPages;
    private int number;
    
    public PageInfo(Page<?> page) {
        this.totalElements = (int) page.getTotalElements();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
    }
} 