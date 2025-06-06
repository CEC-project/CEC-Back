package com.backend.server.api.user.equipment.controller;

import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListResponse;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelResponse;
import com.backend.server.api.user.equipment.service.EquipmentModelService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-models")
@RequiredArgsConstructor
@Tag(name = "1-2. 대여 신청 / 장비 모델", description = "수정 완료")
public class EquipmentModelController {

    private final EquipmentModelService equipmentModelService;

    @GetMapping
    @Operation(
            summary = "장비 모델 목록 조회"
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(
            @ParameterObject @ModelAttribute EquipmentModelListRequest request
    ) {
        return ApiResponse.success("장비 모델 목록 조건에 따른 조회 성공", equipmentModelService.getAllModels(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 모델 단일 조회"
            )
    public ApiResponse<EquipmentModelResponse> getModel(
            @PathVariable Long id
    ) {
        return ApiResponse.success("장비 모델 상세 조회 성공", equipmentModelService.getModel(id));
    }
}