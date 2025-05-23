package com.backend.server.api.admin.equipment.service;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;
import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryIdResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional
public class AdminEquipmentCategoryService {

    private final EquipmentCategoryRepository categoryRepository;
    //중복검사
    public void checkExist(AdminEquipmentCategoryCreateRequest request){
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }
        if (categoryRepository.existsByEnglishCode(request.getEnglishCode())) {
            throw new IllegalArgumentException("이미 존재하는 영문 코드입니다.");
        }
    }

    //업데이트 중복검사
    private void checkExistForUpdate(Long id, AdminEquipmentCategoryCreateRequest request) {
        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }
        if (categoryRepository.existsByEnglishCodeAndIdNot(request.getEnglishCode(), id)) {
            throw new IllegalArgumentException("이미 존재하는 영문 코드입니다.");
        }
    }
    // 카테고리 생성
    public AdminEquipmentCategoryIdResponse createCategory(AdminEquipmentCategoryCreateRequest request) {
        checkExist(request);
        EquipmentCategory savedCategory = categoryRepository.save(request.toEntity());
        return new AdminEquipmentCategoryIdResponse(savedCategory.getId());
    }

    //카테고리 수정
    public AdminEquipmentCategoryIdResponse updateCategory(Long id, AdminEquipmentCategoryCreateRequest request) {
        checkExistForUpdate(id, request);
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));

        category = category.toBuilder()
                .name(request.getName())
                .maxRentalCount(request.getMaxRentalCount())
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