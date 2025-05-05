package com.backend.server.api.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.dto.equipment.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.dto.equipment.model.AdminEquipmentModelIdResponse;
import com.backend.server.api.admin.service.AdminEquipmentModelService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.dto.equipment.equipment.EquipmentModelListRequest;
import com.backend.server.api.user.dto.equipment.equipment.EquipmentModelListResponse;
import com.backend.server.api.user.dto.equipment.equipment.EquipmentModelResponse;
import com.backend.server.api.user.service.EquipmentModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/equipment-models")
@RequiredArgsConstructor
@Tag(name = "장비 모델 어드민 API", description = "장비 모델 조회 관련 API")
public class AdminEquipmentModelController {

    private final AdminEquipmentModelService adminEquipmentModelService;
    private final EquipmentModelService equipmentModelService;

    // 장비 모델 생성
    @PostMapping
    @Operation(
        summary = "장비 모델 생성",
        description = "새로운 장비 모델을 생성합니다. 모델명, 영문 코드, 가용 여부, 카테고리 ID를 입력받습니다."
    )
    public ApiResponse<AdminEquipmentModelIdResponse> createModel(
            @Valid @RequestBody 
            @Parameter(
                description = "생성할 장비 모델의 정보 (모델명, 영문 코드, 가용 여부, 카테고리 ID)"
            ) AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 생성 성공", adminEquipmentModelService.createModel(request));
    }

    // 장비 모델 수정
    @PutMapping("/{id}")
    @Operation(
        summary = "장비 모델 수정",
        description = "기존 장비 모델 정보를 수정합니다. 수정할 모델의 ID와 새로운 정보를 입력받습니다."
    )
    public ApiResponse<AdminEquipmentModelIdResponse> updateModel(
            @Parameter(description = "수정할 장비 모델의 ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody 
            @Parameter(description = "수정할 장비 모델 정보 (모델명, 영문 코드, 가용 여부, 카테고리 ID)") 
            AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 수정 성공", adminEquipmentModelService.updateModel(id, request));
    }

    // 장비 모델 삭제
    @DeleteMapping("/{id}")
    @Operation(
        summary = "장비 모델 삭제",
        description = "지정한 ID에 해당하는 장비 모델을 삭제합니다."
    )
    public ApiResponse<AdminEquipmentModelIdResponse> deleteModel(
            @Parameter(description = "삭제할 장비 모델의 ID", example = "1")
            @PathVariable Long id) {
        return ApiResponse.success("장비 모델 삭제 성공", adminEquipmentModelService.deleteModel(id));
    }

    //====================================================유저 서비스스에서 가져옴====================================================
    @GetMapping
    @Operation(
        summary = "장비 모델 목록 조회",
        description = "장비 모델을 페이지네이션과 검색 조건(모델명, 영문 코드 등)에 따라 조회합니다. " +
                    "정렬 기준, 키워드, 페이지 번호, 페이지 크기를 설정할 수 있습니다."
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(EquipmentModelListRequest request) {
        return ApiResponse.success("장비 모델 전체 조회 성공", equipmentModelService.getAllModels(request));
    }


    @GetMapping("/{id}")
    @Operation(
        summary = "장비 모델 상세 조회",
        description = "지정한 ID에 해당하는 장비 모델의 상세 정보를 조회합니다. " +
                    "모델명, 영문 코드, 가용성 상태, 관련 카테고리 정보 등을 반환합니다."
    )
    public ApiResponse<EquipmentModelResponse> getModelById(@PathVariable Long id) {
        return ApiResponse.success("장비 모델 단건 조회 성공", equipmentModelService.getModel(id));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "카테고리에 속한 장비 모델 목록 조회",
        description = "지정한 카테고리에 속한 모든 장비 모델 목록을 조회합니다."
    )
    public ApiResponse<EquipmentModelListResponse> getModelsByCategory(
            @Parameter(description = "카테고리 ID", example = "1")
            @PathVariable Long categoryId) {
        
        return ApiResponse.success(
            "카테고리별 장비 모델 조회 성공", 
            equipmentModelService.getModelsByCategory(categoryId)
        );
    }
}