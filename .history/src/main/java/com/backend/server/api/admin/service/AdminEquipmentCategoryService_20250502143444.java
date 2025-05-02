package com.backend.server.api.admin.service;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.EquipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEquipmentCategoryService {

    private final EquipmentCategoryRepository categoryRepository;

    

    // 생성
    public EquipmentCategory createCategory(EquipmentCategory category) {
        return categoryRepository.save(category);
    }

    // 수정
    public EquipmentCategory updateCategory(Long id, EquipmentCategory updatedCategory) {
        EquipmentCategory category = getCategoryById(id);
        category = category.toBuilder()
                .name(updatedCategory.getName())
                .englishCode(updatedCategory.getEnglishCode())
                .build();
        return categoryRepository.save(category);
    }

    // 삭제
    public void deleteCategory(Long id) {
        EquipmentCategory category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}