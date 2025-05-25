package com.backend.server.api.user.equipment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListResponse;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelResponse;
import com.backend.server.api.user.equipment.service.EquipmentModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-models")
@RequiredArgsConstructor
@Tag(name = "\uD83D\uDCF7 장비 모델 API", description = "장비 모델 조회 관련 API")
public class EquipmentModelController {
    private final EquipmentModelService equipmentModelService;

    //장비 모델 검색 / 페이지네이션 조회
    // 장비 모델 목록 조회 API
    @GetMapping
    @Operation(
            summary = "장비 모델 목록 조회",
            description = """
        장비 모델을 조건에 따라 조회합니다. 페이징, 정렬, 검색 기능을 함께 사용할 수 있습니다.

        <b>검색 필터:</b><br>
        - <code>categoryId</code>: 특정 장비 카테고리 ID로 필터링<br>
        - <code>keyword</code>: 모델명 또는 영문 코드에 대한 검색 키워드 (부분 일치)<br>

        <b>⬇정렬 조건:</b><br>
        - <code>sortBy</code>: 정렬 기준 필드명 (예: name, createdAt 등)<br>
        - <code>sortDirection</code>: 정렬 방향 (asc 또는 desc, 기본 asc)<br>

        <b>페이징 조건:</b><br>
        - <code>page</code>: 페이지 번호 (0부터 시작)<br>
        - <code>size</code>: 페이지당 항목 수<br>

        모든 파라미터는 선택값이며, 조합하여 사용할 수 있습니다. 밑에 require는 무시!!
        예시: <code>?categoryId=1&keyword=카메라&sortBy=name&sortDirection=desc&page=0&size=10</code>
        """
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(
            @Parameter
            @ModelAttribute EquipmentModelListRequest request
    ) {
        return ApiResponse.success("장비 모델 목록 조건데 따른 조회 성공", equipmentModelService.getAllModels(request));
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
