package com.backend.server.api.admin.service;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.EquipmentCategoryRepository;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEquipmentCategoryService {

    private final EquipmentCategoryRepository categoryRepository;

    public AdminEquipmentCategoryIdResponse createCategory(AdminEquipmentCategoryCreateRequest request) {
        EquipmentCategory savedCategory = categoryRepository.save(request);
        return new AdminEquipmentCategoryIdResponse(savedCategory);
    }

    public AdminEquipmentCategoryIdResponse updateCategory(Long id, AdminEquipmentCategoryCreateRequest request) {
        EquipmentCategory category = getCategoryById(id);
        category = category.toBuilder()
                .name(updatedCategory.getName())
                .englishCode(updatedCategory.getEnglishCode())
                .build();
        EquipmentCategory savedCategory = categoryRepository.save(category);
        return new AdminEquipmentCategoryIdResponse(savedCategory);
    }

    public void deleteCategory(Long id) {
        EquipmentCategory category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    private EquipmentCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
    }
}