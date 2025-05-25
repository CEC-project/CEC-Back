package com.backend.server.api.admin.equipment.controller;

import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "장비 모델 어드민 API", description = "어드민 권한으로 장비 모델을 생성, 수정, 삭제하거나 목록을 조회할 수 있는 API입니다.")
public class AdminEquipmentModelController {

    private final AdminEquipmentModelService adminEquipmentModelService;
    private final EquipmentModelService equipmentModelService;

    @PostMapping
    @Operation(
            summary = "장비 모델 생성",
            description = """
        새로운 장비 모델을 등록합니다.

        필수 입력값:
        - name: 장비 모델명 (예: 소니 미러리스 A7M4)
        - englishCode: 장비의 고유 영문 코드 (예: SONY_A7M4)
        - available: true/false 여부 (현재 장비 모델 사용 가능 여부)
        - categoryId: 장비가 속한 카테고리의 ID

        주의:
        - 같은 영문 코드는 중복 등록이 불가능합니다.
        - 카테고리 ID는 존재하는 값이어야 합니다.
        """
    )
    public ApiResponse<AdminEquipmentModelIdResponse> createModel(
            @Valid @RequestBody
            @Parameter(description = "생성할 장비 모델 정보") AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 생성 성공", adminEquipmentModelService.createModel(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "장비 모델 수정",
            description = """
        기존 장비 모델 정보를 수정합니다.

        수정 가능한 항목:
        - 모델명(name)
        - 영문 코드(englishCode)
        - 사용 가능 여부(available)
        - 카테고리 ID(categoryId)

        주의:
        - 수정 대상 모델이 존재해야 합니다.
        - 영문 코드는 중복되지 않아야 합니다.
        """
    )
    public ApiResponse<AdminEquipmentModelIdResponse> updateModel(
            @Parameter(description = "수정할 장비 모델의 고유 ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody
            @Parameter(description = "수정할 장비 모델 정보") AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 수정 성공", adminEquipmentModelService.updateModel(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "장비 모델 삭제",
            description = """
        지정한 ID에 해당하는 장비 모델을 삭제합니다.

        삭제 전 유의사항:
        - 해당 모델에 등록된 장비가 하나라도 존재하면 삭제가 불가할 수 있습니다.
        - 논리 삭제가 아닌 물리 삭제입니다.
        """
    )
    public ApiResponse<AdminEquipmentModelIdResponse> deleteModel(
            @Parameter(description = "삭제할 장비 모델의 고유 ID", example = "1")
            @PathVariable Long id) {
        return ApiResponse.success("장비 모델 삭제 성공", adminEquipmentModelService.deleteModel(id));
    }

    @GetMapping
    @Operation(
            summary = "장비 모델 목록 조회",
            description = """
        등록된 장비 모델 목록을 필터 조건과 함께 조회합니다.

        필터링 및 정렬 옵션:
        - categoryId: 특정 카테고리 ID로 필터링
        - keyword: 모델명 또는 영문 코드에 포함된 키워드 (예: "카메라", "CANON", "CAM01")
        - page: 페이지 번호 (기본값 0)
        - size: 페이지당 항목 수 (기본값 10)
        - sortBy: 정렬 기준 필드 (예: name, createdAt, id)
        - sortDirection: 정렬 방향 (ASC 또는 DESC)

        예시:
        /api/admin/equipment-models?categoryId=1&keyword=카메라&sortBy=name&sortDirection=ASC
        """
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(
            @ParameterObject EquipmentModelListRequest request) {
        return ApiResponse.success("장비 모델 전체 조회 성공", equipmentModelService.getAllModels(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 모델 단건 조회",
            description = """
        특정 ID를 기준으로 장비 모델의 상세 정보를 조회합니다.

        반환 항목:
        - 모델명
        - 영문 코드
        - 사용 가능 여부
        - 소속 카테고리 정보
        - 등록일 및 기타 부가 정보
        """
    )
    public ApiResponse<EquipmentModelResponse> getModelById(
            @Parameter(description = "조회할 장비 모델의 고유 ID", example = "1") @PathVariable Long id) {
        return ApiResponse.success("장비 모델 단건 조회 성공", equipmentModelService.getModel(id));
    }

}
