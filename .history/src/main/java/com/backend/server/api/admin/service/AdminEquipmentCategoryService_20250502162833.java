package com.backend.server.api.admin.service;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.EquipmentCategoryRepository;
import com.backend.server.api.admin.dto.equipment.category.AdminEquipmentCategoryCreateRequest;
import com.backend.server.api.admin.dto.equipment.category.AdminEquipmentCategoryIdResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional
public class AdminEquipmentCategoryService {

    private final EquipmentCategoryRepository categoryRepository;

    // 카테고리 생성
    public AdminEquipmentCategoryIdResponse createCategory(AdminEquipmentCategoryCreateRequest request) {
        EquipmentCategory savedCategory = categoryRepository.save(request.toEntity());
        return new AdminEquipmentCategoryIdResponse(savedCategory.getId());
    }

    //카테고리 수정
    public AdminEquipmentCategoryIdResponse updateCategory(Long id, AdminEquipmentCategoryCreateRequest request) {
        EquipmentCategory category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
            
        category = category.toBuilder()
                .name(request.getName())
                .englishCode(request.getEnglishCode())
                .build();
            
        EquipmentCategory savedCategory = categoryRepository.save(category);
        return new AdminEquipmentCategoryIdResponse(savedCategory.getId());
    }

    //카테고리 삭제
    public AdminEquipmentCategoryIdResponse deleteCategory(Long id) {
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
        categoryRepository.delete(category);
        return new AdminEquipmentCategoryIdResponse(category.getId());
    }
}