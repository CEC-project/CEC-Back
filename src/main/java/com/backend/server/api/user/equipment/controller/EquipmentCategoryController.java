package com.backend.server.api.user.equipment.controller;

import java.util.List;

import com.backend.server.api.user.equipment.dto.category.EquipmentCategoryResponse;
import com.backend.server.api.user.equipment.dto.category.EquipmentCountByCategoryResponse;
import com.backend.server.api.user.equipment.service.EquipmentCategoryService;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "장비 카테고리", description = "장비 분류 정보 조회 API입니다. 전체 카테고리 목록, 특정 카테고리 조회, 카테고리별 장비 수를 확인할 수 있습니다.")
@RestController
@RequestMapping("/api/equipment-categories")
@RequiredArgsConstructor
public class EquipmentCategoryController {

    private final EquipmentCategoryService categoryService;

    @GetMapping
    @Operation(
            summary = "전체 카테고리 조회",
            description = """
        시스템에 등록된 모든 장비 카테고리 목록을 조회합니다.

        반환 데이터에는 카테고리 ID, 이름, 설명 등이 포함됩니다.

        예: "카메라", "삼각대", "마이크" 등의 분류 정보를 확인할 수 있습니다.
        """
    )
    public ApiResponse<List<EquipmentCategoryResponse>> getAllCategories() {
        return ApiResponse.success("카테고리 조회 성공", categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "카테고리 상세 조회",
            description = """
        특정 카테고리 ID를 통해 해당 장비 분류의 상세 정보를 조회합니다.

        사용 예:
        - `/api/equipment-categories/1`

        반환 정보:
        - 카테고리명
        - 설명 등
        """
    )
    public ApiResponse<EquipmentCategoryResponse> getCategoryById(
            @Parameter(description = "조회할 장비 카테고리의 ID", example = "1")
            @PathVariable Long id) {
        return ApiResponse.success("특정아이디 장비 카테고리 조회", categoryService.getCategoryById(id));
    }

    @GetMapping("/countbycategory")
    @Operation(
            summary = "카테고리별 장비 개수 통계",
            description = """
        각 장비 카테고리별로 다음 정보를 통계로 제공합니다:

        - 총 등록된 장비 수
        - 대여 가능한 장비 수
        - 고장/파손된 장비 수

        예:  
        - 카메라: 총 20대, 사용 가능 15대, 고장 5대  
        - 삼각대: 총 10대, 사용 가능 8대, 고장 2대  
        
        프론트엔드에서는 이 API를 통해 카테고리별 대여 상태를 시각적으로 표시할 수 있습니다.
        """
    )
    public ApiResponse<List<EquipmentCountByCategoryResponse>> countEquipment() {
        return ApiResponse.success("카테고리별 장비 개수 조회 성공", categoryService.countAllCategoryWithEquipment());
    }
}
