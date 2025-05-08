package com.backend.server.api.admin.classRoom;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.api.user.dto.equipment.equipmentCategory.EquipmentCategoryResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCategoryResponse;
import com.backend.server.api.admin.service.AdminEquipmentCategoryService;
import com.backend.server.api.user.service.EquipmentCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Tag(name = "관리자 장비 카테고리", description = "관리자용 장비 카테고리 관리 API")
@RestController
@RequestMapping("/api/equipment-categories")
@RequiredArgsConstructor
public class AdminEquipmentCategoryController {

    private final AdminEquipmentCategoryService adminEquipmentCategoryService;
    private final EquipmentCategoryService equipmentCategoryService;

    @Operation(summary = "카테고리 생성", description = "새로운 장비 카테고리를 생성합니다.")
    @PostMapping
    public AdminEquipmentCategoryIdResponse createCategory(
        @Parameter(description = "생성할 카테고리 정보")
        @RequestBody EquipmentCategory category) {
        return adminEquipmentCategoryService.createCategory(category);
    }

    @Operation(summary = "카테고리 수정", description = "기존 장비 카테고리를 수정합니다.")
    @PutMapping("/{id}")
    public AdminEquipmentCategoryIdResponse updateCategory(
        @Parameter(description = "카테고리 ID", example = "1")
        @PathVariable Long id,
        @Parameter(description = "수정할 카테고리 정보")
        @RequestBody EquipmentCategory category) {
        return adminEquipmentCategoryService.updateCategory(id, category);
    }

    @Operation(summary = "카테고리 삭제", description = "장비 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deleteCategory(
        @Parameter(description = "카테고리 ID", example = "1")
        @PathVariable Long id) {
        adminEquipmentCategoryService.deleteCategory(id);
    }

    @Operation(summary = "전체 카테고리 조회", description = "모든 장비 카테고리를 조회합니다.")
    @GetMapping
    public List<EquipmentCategoryResponse> getAllCategories() {
        return equipmentCategoryService.getAllCategories();
    }

    @Operation(summary = "카테고리 상세 조회", description = "특정 ID의 장비 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public EquipmentCategoryResponse getCategoryById(
        @Parameter(description = "카테고리 ID", example = "1")
        @PathVariable Long id) {
        return equipmentCategoryService.getCategoryById(id);
    }
}

