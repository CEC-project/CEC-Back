package com.backend.server.api.admin.equipment.controller;

import java.util.List;

import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.equipment.dto.equipment.response.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/equipments")
@Tag(name = "관리자 장비 API", description = "관리자 권한으로 장비 등록, 수정, 삭제, 검색 및 상태 변경 등을 처리하는 API입니다.")
public class AdminEquipmentController {

    private final AdminEquipmentService adminEquipmentService;

    @GetMapping("/managers")
    @Operation(summary = "관리자 목록 조회", description = "장비 등록 또는 관리 권한을 가진 관리자 계정 목록을 조회합니다.")
    public ApiResponse<List<AdminManagerCandidatesResponse>> getAdminUsers() {
        return ApiResponse.success("관리자 목록 조회 성공", adminEquipmentService.getAdminUsers());
    }

    @PostMapping
    @Operation(
            summary = "장비 등록",
            description = """
            새 장비를 시스템에 등록합니다.
    
            입력 항목에는 이미지 경로, 카테고리 ID, 모델 ID, 수량, 관리자 ID, 설명, 제한 학년 등이 포함됩니다.
            """

    )
    public ApiResponse<List<Long>> createEquipment(
            @RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 등록 성공", adminEquipmentService.createEquipment(request));
    }

    @GetMapping("/get-serial_number")
    @Operation(
            summary = "시리얼 넘버 생성",
            description = "등록할 장비 조건에 기반해 첫 번째 장비에 부여될 시리얼 넘버를 미리 확인합니다."
    )
    public ApiResponse<String> getSerialNumber(@ModelAttribute AdminEquipmentSerialNumberGenerateRequest request) {
        return ApiResponse.success("시리얼넘버 보여주기 성공", adminEquipmentService.generateSerialNumber(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "장비 정보 수정",
            description = "기존 장비의 세부 정보를 수정합니다. 이미지, 모델, 카테고리, 관리자, 설명, 제한 학년 등을 변경할 수 있습니다."
    )
    public ApiResponse<Long> updateEquipment(
            @PathVariable Long id,
            @RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 수정 성공", adminEquipmentService.updateEquipment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "장비 삭제",
            description = "장비를 시스템에서 완전히 삭제합니다. 삭제된 장비는 복구할 수 없습니다."
    )
    public ApiResponse<Long> deleteEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 삭제 성공", adminEquipmentService.deleteEquipment(id));
    }

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

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 단일 조회",
            description = "장비 ID로 해당 장비의 상세 정보를 조회합니다."
    )
    public ApiResponse<AdminEquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세조회 성공", adminEquipmentService.getEquipment(id));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "장비 상태 변경",
            description = "지정한 장비의 상태를 변경합니다. (예: AVAILABLE, IN_USE, BROKEN 등)"
    )
    public ApiResponse<Long> updateEquipmentStatus(
            @PathVariable Long id,
            @RequestBody AdminEquipmentStatusUpdateRequest request) {
        adminEquipmentService.updateEquipmentStatus(id, request);
        return ApiResponse.success("장비 상태 변경 성공", id);
    }

    @PutMapping("/status")
    @Operation(
            summary = "장비 상태 다중 변경",
            description = "지정한 장비들의 상태를 변경합니다. (예: AVAILABLE, IN_USE, BROKEN 등)"
    )
    public ApiResponse<List<Long>> updateMultipleEquipmentStatus(
            @RequestBody AdminEquipmentStatusMultipleUpdateRequest request) {

        return ApiResponse.success("장비 상태 변경 성공", adminEquipmentService.updateMultipleEquipmentStatus(request));
    }

    @PostMapping("/approve")
    @Operation(
            summary = "대여 요청 승인",
            description = "대여 요청(RENTAL_PENDING) 상태의 장비를 승인하여 IN_USE 상태로 변경합니다."
    )
    public ApiResponse<Void> approveRentalRequests(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.approveRentalRequests(equipmentIds);
        return ApiResponse.success("대여 요청 승인 성공", null);
    }

    @PostMapping("/reject")
    @Operation(
            summary = "대여 요청 거절",
            description = "RENTAL_PENDING 상태의 장비를 거절하고 AVAILABLE 상태로 되돌립니다."
    )
    public ApiResponse<Void> rejectRentalRequests(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.rejectRentalRequests(equipmentIds);
        return ApiResponse.success("대여 요청 거절 성공", null);
    }


//    @PostMapping("/return")
//    @Operation(
//            summary = "반납 처리",
//            description = "RETURN_PENDING 또는 IN_USE 상태의 장비를 AVAILABLE 상태로 변경합니다."
//    )
//    public ApiResponse<Void> processReturnRequests(@RequestBody List<Long> equipmentIds) {
//        adminEquipmentService.processReturnRequests(equipmentIds);
//        return ApiResponse.success("반납 처리 성공", null);
//    }


    @PostMapping("/broken")
    @Operation(
            summary = "장비 고장/파손",
            description = "지정된 장비들을 BROKEN 상태로 변경하며, 파손 사유를 함께 기록할 수 있습니다. 사유는 장비의 설명에 추가로 붙습니다"
    )
    public ApiResponse<Void> markEquipmentsAsBroken(
            @RequestBody MarkEquipmentsAsBrokenRequest request) {
        adminEquipmentService.markEquipmentsAsBroken(request.getEquipmentIds(), request.getDescription());
        return ApiResponse.success("장비 고장/파손 처리 성공", null);
    }

    @PostMapping("/repair")
    @Operation(
            summary = "장비 수리",
            description = "BROKEN 상태의 장비를 AVAILABLE 상태로 변경하며 수리 내용을 기록할 수 있습니다. 내용은 장비의 설명에 추가로 붙습니다"
    )
    public ApiResponse<Void> repairEquipments(
            @RequestBody RepairEquipmentsRequest request) {
        adminEquipmentService.repairEquipments(request.getEquipmentIds(), request.getDescription());
        return ApiResponse.success("장비 복구 처리 성공", null);
    }

    @PostMapping("/extend")
    @Operation(
            summary = "대여 기간 연장",
            description = "대여 중인 장비들의 반납 기한을 새로운 날짜로 연장합니다."
    )
    public ApiResponse<Void> extendRentalPeriods(
            @RequestBody ExtendRentalPeriodsRequest request) {
        adminEquipmentService.extendRentalPeriods(request.getEquipmentIds(), request.getNewEndDate());
        return ApiResponse.success("대여 기간 연장 성공", null);
    }


    @PostMapping("/force-return")
    @Operation(
            summary = "강제 회수",
            description = "대여 중인 장비들을 강제로 회수하여 AVAILABLE 상태로 변경합니다."
    )
    public ApiResponse<Void> forceReturnEquipments(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.forceReturnEquipments(equipmentIds);
        return ApiResponse.success("강제 회수 성공", null);
    }
}
