package com.backend.server.api.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.user.dto.equipment.equipmentCategory.EquipmentCategoryResponse;
import com.backend.server.api.user.service.EquipmentCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "장비 카테고리", description = "장비 카테고리 관련 API")
@RestController
@RequestMapping("/api/equipment-categories")
@RequiredArgsConstructor
public class EquipmentCategoryController {

    private final EquipmentCategoryService categoryService;

    @Operation(summary = "전체 카테고리 조회", description = "모든 장비 카테고리를 조회합니다.")
    @GetMapping
    public List<EquipmentCategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "카테고리 상세 조회", description = "특정 ID의 장비 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public EquipmentCategoryResponse getCategoryById(
        @Parameter(description = "카테고리 ID", example = "1") 
        @PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}