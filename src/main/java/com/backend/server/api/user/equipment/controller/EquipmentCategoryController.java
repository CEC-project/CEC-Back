package com.backend.server.api.user.equipment.controller;

import java.util.List;

import com.backend.server.api.user.equipment.dto.category.EquipmentCategoryListResponse;
import com.backend.server.api.user.equipment.dto.category.EquipmentCategoryResponse;
import com.backend.server.api.user.equipment.service.EquipmentCategoryService;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "1-1. 대여 신청 / 장비 분류", description = "수정 완료")
@RestController
@RequestMapping("/api/equipment-categories")
@RequiredArgsConstructor
public class EquipmentCategoryController {

    private final EquipmentCategoryService categoryService;


    @GetMapping("/{id}")
    @Operation(
            summary = "카테고리 단일 조회"
    )
    public ApiResponse<EquipmentCategoryResponse> getCategoryById(
            @Parameter(description = "조회할 장비 카테고리의 ID", example = "1")
            @PathVariable Long id) {
        return ApiResponse.success("특정아이디 장비 카테고리 조회", categoryService.getCategoryById(id));
    }

    @GetMapping()
    @Operation(
            summary = "카테고리 전체 조회와 각 카테고리 별 장비 개수 통계"
    )
    public ApiResponse<List<EquipmentCategoryListResponse>> countEquipment() {
        return ApiResponse.success("카테고리별 장비 개수 조회 성공", categoryService.countAllCategoryWithEquipment());
    }
}
