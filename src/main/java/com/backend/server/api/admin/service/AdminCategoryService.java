package com.backend.server.api.admin.service;

import com.backend.server.api.common.dto.CategoryResponse;
import com.backend.server.model.entity.Category;
import com.backend.server.model.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse createCategory(CategoryResponse categoryDto) {
        // 동일 이름의 카테고리가 있는지 확인
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + categoryDto.getName());
        }

        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();

        category = categoryRepository.save(category);
        return convertToDto(category);
    }

    /**
     * 카테고리 정보를 수정합니다.
     *
     * @param id 카테고리 ID
     * @param categoryDto 수정할 카테고리 정보
     * @return 수정된 카테고리 DTO
     */
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryResponse categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));

        // 이름이 변경됐고, 변경하려는 이름이 이미 존재하는 경우 체크
        if (!category.getName().equals(categoryDto.getName()) && 
                categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + categoryDto.getName());
        }

        Category updatedCategory = Category.builder()
                .id(category.getId())
                .name(categoryDto.getName())
                .build();

        updatedCategory = categoryRepository.save(updatedCategory);
        return convertToDto(updatedCategory);
    }

    /**
     * 카테고리를 삭제합니다.
     *
     * @param id 삭제할 카테고리 ID
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));
        
        categoryRepository.delete(category);
    }

    /**
     * Category 엔티티를 CategoryDto로 변환합니다.
     *
     * @param category 카테고리 엔티티
     * @return 카테고리 DTO
     */
    private CategoryResponse convertToDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
} 