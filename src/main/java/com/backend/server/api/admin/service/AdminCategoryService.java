package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.category.AdminClassRoomCreateRequest;
import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.api.common.service.CommonCategoryReadService;
import com.backend.server.model.entity.Category;
import com.backend.server.model.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CommonCategoryReadService categoryReadService;
    
    //카테고리 조회. 이건 유저도 공통 기능을 가지니까 CategoryResponse를 씀.
    @Transactional(readOnly = true)
    public List<CommonCategoryResponse> getAllCategories() {
        return categoryReadService.getAllCategories();
    }

    //카테고리 만들기. 이건 어드민만
    @Transactional
    public CommonCategoryResponse createCategory(AdminClassRoomCreateRequest request) {
        // 동일 이름의 카테고리가 있는지 확인
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + request.getName());
        }

        Category category = request.toEntity();
        category = categoryRepository.save(category);
        return new CommonCategoryResponse(category);
    }

    //카테고리 수정. 이것도 어드민만 됌.
    @Transactional
    public CommonCategoryResponse updateCategory(Long id, AdminClassRoomCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));

        // 이름이 변경됐고, 변경하려는 이름이 이미 존재하는 경우 체크
        if (!category.getName().equals(request.getName()) && 
                categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + request.getName());
        }

        Category updatedCategory = Category.builder()
                .id(category.getId())
                .name(request.getName())
                .build();

        updatedCategory = categoryRepository.save(updatedCategory);
        return new CommonCategoryResponse(updatedCategory);
    }

    //카테고리 삭제
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));
        
        categoryRepository.delete(category);
    }
} 