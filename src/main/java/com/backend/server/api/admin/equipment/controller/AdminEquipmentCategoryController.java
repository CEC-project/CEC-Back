package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.user.equipment.dto.category.EquipmentCountByCategoryResponse;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;
import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryIdResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentCategoryService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.equipment.dto.category.EquipmentCategoryResponse;
import com.backend.server.api.user.equipment.service.EquipmentCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Tag(name = "\uD83D\uDCF7 관리자 장비 카테고리", description = "관리자용 장비 카테고리 관리 API")
@RestController
@RequestMapping("/api/admin/equipment-categories")
@RequiredArgsConstructor
public class AdminEquipmentCategoryController {

    private final AdminEquipmentCategoryService adminEquipmentCategoryService;
    private final EquipmentCategoryService equipmentCategoryService;


    @Operation(summary = "카테고리 생성", description = "새로운 장비 카테고리를 생성합니다.")
    @PostMapping
    public ApiResponse<AdminEquipmentCategoryIdResponse> createCategory(
        @Parameter(description = "생성할 카테고리 정보")
        @Valid @RequestBody AdminEquipmentCategoryCreateRequest request) {
        return ApiResponse.success("카테고리 생성 성공", adminEquipmentCategoryService.createCategory(request));
    }


    @Operation(summary = "카테고리 수정", description = "기존 장비 카테고리를 수정합니다.")
    @PutMapping("/{id}")
    public ApiResponse<AdminEquipmentCategoryIdResponse> updateCategory(
        @Parameter(description = "카테고리 ID", example = "1")
        @PathVariable Long id,
        @Parameter(description = "수정할 카테고리 정보")
        @Valid @RequestBody AdminEquipmentCategoryCreateRequest request) {
        return ApiResponse.success("카테고리 수정 성공", adminEquipmentCategoryService.updateCategory(id, request));
    }

    @Operation(summary = "카테고리 삭제", description = "장비 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<AdminEquipmentCategoryIdResponse> deleteCategory(
        @Parameter(description = "카테고리 ID", example = "1")
        @PathVariable Long id) {
        return ApiResponse.success("카테고리 삭제 성공",adminEquipmentCategoryService.deleteCategory(id) );
    }

    //====================================================유저에서 가져옴====================================================

    @Operation(summary = "전체 카테고리 조회", description = "모든 장비 카테고리를 조회합니다.")
    @GetMapping
    public ApiResponse<List<EquipmentCategoryResponse>> getAllCategories() {
        return ApiResponse.success("전체 카테고리 조회 성공", equipmentCategoryService.getAllCategories());
    }

    @Operation(summary = "카테고리 상세 조회", description = "특정 ID의 장비 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<EquipmentCategoryResponse> getCategoryById(
        @Parameter(description = "카테고리 ID", example = "1")
        @PathVariable Long id) {
        return ApiResponse.success("카테고리 상세 조회 성공", equipmentCategoryService.getCategoryById(id));
    }

    @Operation(summary = "카테고리 전체랑 그에 따른 총 장비, 사용가능장비, 파손된 장비 등등 표시하는거")
    @GetMapping("/countbycategory")
    public ApiResponse<List<EquipmentCountByCategoryResponse>> countEquipment() {
        return ApiResponse.success("카테고리별 장비 개수 조회 성공",equipmentCategoryService.countAllCategoryWithEquipment());
    }


}

