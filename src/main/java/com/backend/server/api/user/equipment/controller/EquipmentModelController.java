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
@Tag(name = "1-2. 대여 신청 / 장비 모델", description = "수정 필요")
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
            summary = "장비 모델 단일 조회",
            description = """
            지정한 ID에 해당하는 장비 모델의 상세 정보를 조회합니다.
            
            반환 항목:
            - 아이디
            - 모델명
            - 영문 코드
            - 사용 가능 여부
            - 카테고리 아이디
            """
            )
    public ApiResponse<EquipmentModelResponse> getModel(
            @Parameter(description = "조회할 장비 모델의 고유 ID", example = "1")
            @PathVariable Long id
    ) {
        return ApiResponse.success("장비 모델 상세 조회 성공", equipmentModelService.getModel(id));
    }
}