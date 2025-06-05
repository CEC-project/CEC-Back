package com.backend.server.api.admin.equipment.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.equipment.service.AdminEquipmentModelService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListResponse;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelResponse;
import com.backend.server.api.user.equipment.service.EquipmentModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/equipment-models")
@RequiredArgsConstructor
@Tag(name = "3-1. 강의실/장비 관리 / 장비 관리 / 모델", description = "수정 완료")
public class AdminEquipmentModelController {

    private final AdminEquipmentModelService adminEquipmentModelService;
    private final EquipmentModelService equipmentModelService;

    @PostMapping
    @Operation(
            summary = "장비 모델 생성"
    )
    public ApiResponse<Long> createModel(
            @Valid @RequestBody
            @Parameter(description = "생성할 장비 모델 정보") AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 생성 성공", adminEquipmentModelService.createModel(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "장비 모델 수정"
    )
    public ApiResponse<Long> updateModel(
            @Parameter(description = "수정할 장비 모델의 고유 ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody
            @Parameter(description = "수정할 장비 모델 정보") AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 수정 성공", adminEquipmentModelService.updateModel(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "장비 모델 삭제"
    )
    public ApiResponse<Long> deleteModel(
            @Parameter(description = "삭제할 장비 모델의 고유 ID", example = "1")
            @PathVariable Long id) {
        return ApiResponse.success("장비 모델 삭제 성공", adminEquipmentModelService.deleteModel(id));
    }

    @GetMapping
    @Operation(
            summary = "장비 모델 목록 조회"
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(
            @ParameterObject EquipmentModelListRequest request) {
        return ApiResponse.success("장비 모델 전체 조회 성공", equipmentModelService.getAllModels(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 모델 단일 조회"
    )
    public ApiResponse<EquipmentModelResponse> getModelById(
            @Parameter(description = "조회할 장비 모델의 고유 ID", example = "1") @PathVariable Long id) {
        return ApiResponse.success("장비 모델 단일 조회 성공", equipmentModelService.getModel(id));
    }
}
