package com.backend.server.api.user.equipment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentModelListRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentModelListResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentModelResponse;
import com.backend.server.api.user.equipment.service.EquipmentModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-models")
@RequiredArgsConstructor
@Tag(name = "장비 모델 API", description = "장비 모델 조회 관련 API")
public class EquipmentModelController {
    private final EquipmentModelService equipmentModelService;

    //장비 모델 검색 / 페이지네이션 조회
    // 장비 모델 목록 조회 API
    @GetMapping
    @Operation(
        summary = "장비 모델 목록 조회",
        description = "장비 모델을 페이지네이션과 검색 조건(모델명, 영문 코드 등)에 따라 조회합니다. " +
                    "정렬 기준, 키워드, 페이지 번호, 페이지 크기를 설정할 수 있습니다."
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(
            @Parameter(
                description = "장비 모델 검색 및 페이지네이션 요청 정보\n" +
                            "- page: 페이지 번호 (0부터 시작)\n" +
                            "- size: 페이지당 항목 수\n" +
                            "- keyword: 모델명 또는 영문 코드 검색 키워드\n" +
                            "- sortBy: 정렬 기준 필드 (예: name)\n" +
                            "- sortDirection: 정렬 방향 (asc 또는 desc) 지정안하면 asc",
                example = "page=0&size=10&keyword=카메라&sortBy=name&sortDirection=asc"
            )
            @ModelAttribute EquipmentModelListRequest request
    ) {
        return ApiResponse.success("장비 모델 목록 조회 성공", equipmentModelService.getAllModels(request));
    }

    // 장비 모델 단건 조회 API
    @GetMapping("/{id}")
    @Operation(
        summary = "장비 모델 상세 조회",
        description = "지정한 ID에 해당하는 장비 모델의 상세 정보를 조회합니다. " +
                    "모델명, 영문 코드, 가용성 상태, 관련 카테고리 정보 등을 반환합니다."
    )
    public ApiResponse<EquipmentModelResponse> getModel(
            @Parameter(
                description = "조회할 장비 모델의 고유 ID",
                required = true,
                example = "1"
            )
            @PathVariable Long id
    ) {
        return ApiResponse.success("장비 모델 상세 조회 성공", equipmentModelService.getModel(id));
    }

}
