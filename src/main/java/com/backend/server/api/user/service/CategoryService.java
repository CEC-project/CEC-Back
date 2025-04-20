package com.backend.server.api.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.api.common.service.CommonCategoryReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CommonCategoryReadService categoryReadService;
    
    // 조회만 가능
    public List<CommonCategoryResponse> getAllCategories() {
        return categoryReadService.getAllCategories();
    }
}
