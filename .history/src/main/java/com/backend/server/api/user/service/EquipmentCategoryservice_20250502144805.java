package com.backend.server.api.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.EquipmentCategoryRepository;
import com.backend.server.api.user.dto.equipment.equipmentCategory.EquipmentCategoryResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentCategoryService{
    
    private final EquipmentCategoryRepository categoryRepository;
    
    @Transactional(readOnly = true)
    public List<EquipmentCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(EquipmentCategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipmentCategoryResponse getCategoryById(Long id) {
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
        return new EquipmentCategoryResponse(category);
    }
}
