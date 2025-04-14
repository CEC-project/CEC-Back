package com.backend.server.api.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.model.entity.Category;
import com.backend.server.model.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonCategoryReadServiceImpl implements CommonCategoryReadService {
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<CommonCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }
    
    //이건 혹시몰라서 만들어놨아요 나중가서도 안쓰면 지울게요
    @Override
    public CommonCategoryResponse getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(Category::toDto)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없음"));
    }
}