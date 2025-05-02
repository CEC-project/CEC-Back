package com.backend.server.api.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.EquipmentCategoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EquipmentCategoryService{
    private final EquipmentCategoryRepository categoryRepository;
    // 전체 조회
    @Transactional(readOnly = true)
    public List<EquipmentCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(EquipmentCategoryResponseDto::new) 
                .collect(Collectors.toList());
    }


    //개별조회 레츠고
    @Transactional(readOnly = true)
    public EquipmentCategoryResponse getCategoryById(Long id) {
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
        return new EquipmentCategoryResponse(category); 
    }
}
