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
@Tag(name = "장비 모델 API", description = "장비 모델 목록 및 단일 모델 조회 기능을 제공합니다.")
public class EquipmentModelController {

    private final EquipmentModelService equipmentModelService;

    @GetMapping
    @Operation(
            summary = "장비 모델 목록 조회",
            description = """
장비 모델 목록을 조회합니다. 카테고리별, 키워드 검색, 정렬, 페이징 등의 조건을 조합하여 사용할 수 있습니다.

---

### 🔍 검색 필터 (선택)

- `categoryId` (Long): 장비 모델의 카테고리 ID로 필터링
- `keyword` (String): 모델명 또는 영문 코드에 포함된 텍스트 검색

---

### 📌 정렬 조건

- `sortBy` (String): 정렬 기준 필드명 (`name`, `createdAt`, `id` 등)
- `sortDirection` (String): 정렬 방향 (`asc` 또는 `desc`, 기본값: asc)

---

### 📄 페이징 조건

- `page` (Integer): 페이지 번호 (0부터 시작)
- `size` (Integer): 한 페이지에 조회할 항목 수

---

### ✅ 예시 요청

GET /api/equipment-models?categoryId=1&keyword=카메라&sortBy=name&sortDirection=desc&page=0&size=10


위 요청은 카테고리 ID가 1인 모델 중 `카메라` 키워드가 포함된 항목을 이름 기준 내림차순 정렬로 0페이지부터 10개씩 조회합니다.
"""
    )
    public ApiResponse<EquipmentModelListResponse> getAllModels(
            @Parameter(description = "장비 모델 목록 조회 조건") @ModelAttribute EquipmentModelListRequest request
    ) {
        return ApiResponse.success("장비 모델 목록 조건에 따른 조회 성공", equipmentModelService.getAllModels(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 모델 상세 조회",
            description = """
지정한 ID에 해당하는 장비 모델의 상세 정보를 조회합니다.

반환 항목:
- 모델명
- 영문 코드
- 사용 가능 여부
- 소속 카테고리 정보 등
"""
    )
    public ApiResponse<EquipmentModelResponse> getModel(
            @Parameter(description = "조회할 장비 모델의 고유 ID", example = "1")
            @PathVariable Long id
    ) {
        return ApiResponse.success("장비 모델 상세 조회 성공", equipmentModelService.getModel(id));
    }
}