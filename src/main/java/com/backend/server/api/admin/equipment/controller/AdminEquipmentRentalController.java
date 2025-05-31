package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentDetailRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.ExtendRentalPeriodsRequest;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentRentalService;
import com.backend.server.api.admin.equipment.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/equipments-rental")
@Tag(name = "장비 대여 관리 API", description = "관리자 권한으로 대여에 관한 항목을 처리하는 API입니다.")
public class AdminEquipmentRentalController {
    private final AdminEquipmentService adminEquipmentService;
    private final AdminEquipmentRentalService adminEquipmentRentalService;

    @GetMapping
    @Operation(
            summary = "장비 목록 조회",
            description = """
            관리자 페이지에서 장비 목록을 **검색, 필터링, 정렬, 페이징 조건**에 따라 조회합니다.
            
            ---
            
            ### 🔍 필터 조건 (선택적 파라미터)
            
            - `categoryId` (Long): 장비 카테고리 ID로 필터링합니다. 예: `1`
            - `modelName` (String): 장비 모델명 일부 또는 전체로 검색합니다. 대소문자 구분 없이 검색됩니다. 예: `SONY`
            - `serialNumber` (String): 장비 일련번호 일부로 검색합니다. 예: `202405231`
            - `status` (String): 장비 상태. 다음 중 하나:
              - `AVAILABLE`: 대여 가능
              - `IN_USE`: 대여 중
              - `RENTAL_PENDING`: 대여 요청됨
              - `RETURN_PENDING`: 반납 요청됨
              - `BROKEN`: 고장/파손 상태
            - `isAvailable` (Boolean): `true`일 경우 현재 대여 가능한 장비만 조회
            - `renterName` (String): 현재 대여자의 이름으로 검색 (부분 일치). 예: `홍길동`
            - `searchKeyword` (String): 모델명, 일련번호, 대여자 이름을 통합하여 검색하는 키워드
            
            ---
            
            ### 📌 정렬 조건 (선택)
            
            - `sortBy` (String): 정렬 기준 필드명. 예:
              - `id`: 장비 ID
              - `createdAt`: 생성일
              - `rentalCount`: 대여 횟수
              - `repairCount`: 수리 횟수
              - `brokenCount`: 고장 횟수 등
            - `sortDirection` (String): 정렬 방향
              - `ASC`: 오름차순
              - `DESC`: 내림차순
            
            ---
            
            ### 📄 페이징 조건 (선택)
            
            - `page` (Integer): 조회할 페이지 번호 (0부터 시작). 기본값: 0
            - `size` (Integer): 페이지당 항목 수. 기본값: 17
            
            ---
            
            ### ✅ 예시 호출
            
            GET /api/admin/equipments?categoryId=1&modelName=sony&sortBy=id&sortDirection=DESC&page=0&size=10
            
            
            위 API는 카테고리 ID가 1이고 모델명에 `sony`가 포함된 장비를 ID 내림차순으로 10개 조회합니다.
            """
    )
    public ApiResponse<AdminEquipmentListResponse> getEquipments(
            @ParameterObject AdminEquipmentListRequest request) {
        return ApiResponse.success("장비 리스트 조회 성공", adminEquipmentService.getEquipments(request));
    }

    @Operation(
            summary = "장비 대여 일괄 상태 변경 API",
            description = """
            - **요청 바디**
              - **equipmentIds** : 필수, 숫자 배열
              - **status** : 필수, RETURN, REJECT, BROKEN, ACCEPT
              - **detail** : 생략 가능, 100자 이내""")
    @PatchMapping("/status")
    public ApiResponse<List<Long>> changeStatus(@Valid @RequestBody AdminEquipmentDetailRequest request) {
        List<Long> ids = adminEquipmentRentalService.changeStatus(request);
        return ApiResponse.success("장비의 상태가 일괄 변경되었습니다.", ids);
    }



    @PostMapping("/extend")
    @Operation(
            summary = "대여 기간 연장",
            description = "대여 중인 장비들의 반납 기한을 새로운 날짜로 연장합니다."
    )
    public ApiResponse<Void> extendRentalPeriods(
            @RequestBody ExtendRentalPeriodsRequest request) {
        adminEquipmentRentalService.extendRentalPeriods(request.getEquipmentIds(), request.getNewEndDate());
        return ApiResponse.success("대여 기간 연장 성공", null);
    }


}
