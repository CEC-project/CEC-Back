package com.backend.server.api.admin.equipment.controller;

import org.springframework.web.bind.annotation.*;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelIdResponse;
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
            description = """
            장비 모델 목록을 조건에 따라 조회합니다. 다음과 같은 필터 및 정렬 옵션을 사용할 수 있습니다:
    
            - `categoryId`: 특정 장비 카테고리 ID로 필터링합니다. 생략하면 전체 카테고리에서 조회합니다.
            - `keyword`: 모델명 또는 영문 코드에 해당 키워드가 포함된 장비만 조회합니다. (예: "카메라", "CAM01")
            - `page`: 페이지 번호 (0부터 시작). 기본값은 0입니다.
            - `size`: 페이지당 항목 수. 기본값은 10입니다.
            - `sortBy`: 정렬 기준 필드명 (예: "name", "createdAt", "id" 등).
            - `sortDirection`: 정렬 방향 ("ASC" 오름차순, "DESC" 내림차순). 기본값은 "DESC"입니다.
    
                모든 파라미터는 선택(optional)이며, 조합하여 사용할 수 있습니다.
    
            예시: `/api/models?categoryId=1&keyword=카메라&page=0&size=10&sortBy=name&sortDirection=ASC`
            """
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