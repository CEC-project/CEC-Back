package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCountByCategoryResponse;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;
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

@Tag(name = "3-1. 강의실/장비 관리 / 장비 관리 / 카테고리", description = "수정 완료")
@RestController
@RequestMapping("/api/admin/equipment-categories")
@RequiredArgsConstructor
public class AdminEquipmentCategoryController {

    private final AdminEquipmentCategoryService adminEquipmentCategoryService;
    private final EquipmentCategoryService equipmentCategoryService;


    @Operation(summary = "카테고리 생성")
    @PostMapping
    public ApiResponse<Long> createCategory(
        @Parameter(description = "생성할 카테고리 정보")
        @Valid @RequestBody AdminEquipmentCategoryCreateRequest request) {
        return ApiResponse.success("카테고리 생성 성공", adminEquipmentCategoryService.createCategory(request));
    }


    @Operation(summary = "카테고리 수정")
    @PutMapping("/{id}")
    public ApiResponse<Long> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody AdminEquipmentCategoryCreateRequest request) {
        return ApiResponse.success("카테고리 수정 성공", adminEquipmentCategoryService.updateCategory(id, request));
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<Long> deleteCategory(
        @PathVariable Long id) {
        return ApiResponse.success("카테고리 삭제 성공",adminEquipmentCategoryService.deleteCategory(id) );
    }

    //====================================================유저에서 가져옴====================================================

    @Operation(summary = "카테고리 상세 조회")
    @GetMapping("/{id}")
    public ApiResponse<EquipmentCategoryResponse> getCategoryById(
        @PathVariable Long id) {
        return ApiResponse.success("카테고리 상세 조회 성공", equipmentCategoryService.getCategoryById(id));
    }

    @Operation(summary = "카테고리 전체랑 그에 따른 총 장비, 사용가능장비, 파손된 장비 등등 표시하는거")
    @GetMapping
    public ApiResponse<List<AdminEquipmentCountByCategoryResponse>> countEquipment() {
        return ApiResponse.success("카테고리별 장비 개수 조회 성공",adminEquipmentCategoryService.countAllCategoryWithEquipment());
    }


}

