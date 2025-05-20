package com.backend.server.api.admin.equipment.controller;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.equipment.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentIdResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentIdsResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentStatusUpdateRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentRentalActionResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.model.entity.enums.Status;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/equipments")
@Tag(name = "관리자 장비 API", description = "장비 모델 조회 관련 API")

public class AdminEquipmentController {
    
    private final AdminEquipmentService adminEquipmentService;
    //어드민 유저 조회 
    @GetMapping("/managers")
    @Operation(summary = "관리자 목록 조회", description = "등록 가능한 관리자 목록을 조회합니다")
    public ApiResponse<List<AdminManagerCandidatesResponse>> getAdminUsers() {
        return ApiResponse.success("관리자 목록 조회 성공", adminEquipmentService.getAdminUsers());
    }

    //장비 등록
    @PostMapping
    @Operation(
        summary = "장비 등록",
        description = "새로운 장비를 등록합니다. 이미지, 카테고리, 모델, 수량, 관리자, 설명, 제한 학년 등 정보를 입력할 수 있습니다."
    )
    public ApiResponse<AdminEquipmentIdsResponse> createEquipment(@RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 등록 성공", adminEquipmentService.createEquipment(request));
    }

    @Operation(summary = "카테고리 시리얼넘버 생성", description = "첫번쨰로 생성되는 장비의 시리얼넘버만 보여줌")
    @GetMapping("/get-serial_number")
    public ApiResponse<String> getSerialNumber(@ModelAttribute AdminEquipmentCreateRequest request){
        return ApiResponse.success("시리얼넘버 보여주기 성공", adminEquipmentService.generateSerialNumber(request));
    }
    @PutMapping("/{id}")
    @Operation(
        summary = "장비 정보 수정",
        description = "기존 장비의 정보를 수정합니다. 이미지, 카테고리, 모델, 관리자, 설명, 제한 학년 등 정보를 변경할 수 있습니다."
    )
    public ApiResponse<AdminEquipmentIdResponse> updateEquipment(
        @PathVariable Long id,
        @RequestBody AdminEquipmentCreateRequest request
    ) {
        return ApiResponse.success("장비 수정 성공", adminEquipmentService.updateEquipment(id, request));
    }

    //장비 삭제
    @DeleteMapping("/{id}")
    @Operation(
        summary = "장비 삭제",
        description = "장비를 삭제합니다. 삭제된 장비는 복구할 수 없습니다."
    )
    public ApiResponse<AdminEquipmentIdResponse> deleteEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 삭제 성공", adminEquipmentService.deleteEquipment(id));
    }

    //장비  리스트 어드민 조회
    @Operation(
            summary = "어드민 - 장비 리스트 조회",
            description = """
        관리자 페이지에서 장비 목록을 검색, 필터링, 정렬, 페이징 조건에 따라 조회합니다.

        <b>🔍 검색/필터 조건:</b><br>
        - <code>categoryId</code>: 장비 카테고리 ID (예: 1)<br>
        - <code>modelName</code>: 모델명 (부분 일치 검색)<br>
        - <code>serialNumber</code>: 장비 일련번호 (부분 일치 검색)<br>
        - <code>status</code>: 장비 상태 (예: AVAILABLE, BROKEN 등)<br>
        - <code>isAvailable</code>: 대여 가능 여부 (true/false)<br>
        - <code>renterName</code>: 현재 대여자 이름 (부분 일치 검색)<br>
        - <code>searchKeyword</code>: 모델명, 일련번호, 대여자 이름에 대한 통합 키워드 검색<br>

        <b>⬇️ 정렬 조건:</b><br>
        - <code>sortBy</code>: 정렬 기준 (예: id, createdAt, rentalCount, repairCount, brokenCount)<br>
        - <code>sortDirection</code>: 정렬 방향 (asc 또는 desc)<br>

        <b>📄 페이징 조건:</b><br>
        - <code>page</code>: 페이지 번호 (0부터 시작)<br>
        - <code>size</code>: 페이지당 항목 수 (기본값: 17)<br>

        ⚠️ 모든 파라미터는 선택(optional)이며, 조건을 조합하여 사용할 수 있습니다.
        """
    )
    @Parameters({
            @Parameter(name = "categoryId", description = "장비 카테고리 ID"),
            @Parameter(name = "modelName", description = "모델명 (부분 일치 검색)"),
            @Parameter(name = "serialNumber", description = "일련번호 (부분 일치 검색)"),
            @Parameter(name = "status", description = "장비 상태 (AVAILABLE, BROKEN 등)"),
            @Parameter(name = "isAvailable", description = "대여 가능 여부 (true/false)"),
            @Parameter(name = "renterName", description = "현재 대여자 이름 (부분 일치 검색)"),
            @Parameter(name = "searchKeyword", description = "모델명, 일련번호, 대여자 이름 통합 검색 키워드"),
            @Parameter(name = "sortBy", description = "정렬 기준 (id, createdAt, rentalCount, repairCount, brokenCount 등)"),
            @Parameter(name = "sortDirection", description = "정렬 방향 (asc 또는 desc)"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지당 항목 수 (기본값: 17)")
    })
    @GetMapping
    public ApiResponse<AdminEquipmentListResponse> getEquipments(
            @ModelAttribute AdminEquipmentListRequest request
    ) {
        return ApiResponse.success("장비 리스트 조회 성공", adminEquipmentService.getEquipments(request));
    }


    //장비 단일 상세조회
    @GetMapping("/{id}")
    @Operation(
        summary = "장비 단일 상세 조회",
        description = "장비 ID로 단일 장비의 상세 정보를 조회합니다."
    )
    public ApiResponse<AdminEquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세조회 성공", adminEquipmentService.getEquipment(id));
    }

    // 장비 상태 변경
    @PutMapping("/{id}/status")
    @Operation(
        summary = "장비 상태 변경",
        description = "장비의 상태를 변경합니다. 대여 가능(AVAILABLE), 대여중(IN_USE), 고장(BROKEN) 등으로 변경할 수 있습니다."
    )
    public ApiResponse<Void> updateEquipmentStatus(
        @PathVariable Long id,
        @RequestBody AdminEquipmentStatusUpdateRequest request
    ) {
        return ApiResponse.success("장비 상태 변경 성공", null);
    }

    // 대여 요청 승인
    @PostMapping("/approve")
    @Operation(
            summary = "대여 요청 승인",
            description = "대여 요청 상태(RENTAL_PENDING)의 장비들을 승인하여 대여중(IN_USE) 상태로 변경합니다."
    )
    public ApiResponse<Void> approveRentalRequests(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.approveRentalRequests(equipmentIds);
        return ApiResponse.success("대여 요청 승인 성공", null);
    }

    // 대여 요청 거절
    @PostMapping("/reject")
    @Operation(
            summary = "대여 요청 거절",
            description = "대여 요청 상태(RENTAL_PENDING)의 장비들을 거절하여 대여 가능(AVAILABLE) 상태로 되돌립니다."
    )
    public ApiResponse<Void> rejectRentalRequests(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.rejectRentalRequests(equipmentIds);
        return ApiResponse.success("대여 요청 거절 성공", null);
    }

    // 반납 처리
    @PostMapping("/return")
    @Operation(
            summary = "반납 처리",
            description = "반납 요청 상태(RETURN_PENDING) 또는 대여중(IN_USE) 상태의 장비들을 반납 처리하여 대여 가능(AVAILABLE) 상태로 변경합니다."
    )
    public ApiResponse<Void> processReturnRequests(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.processReturnRequests(equipmentIds);
        return ApiResponse.success("반납 처리 성공", null);
    }

    // 장비 고장/파손 처리
    @PostMapping("/broken")
    @Operation(
            summary = "장비 고장/파손 처리",
            description = "장비들을 고장/파손(BROKEN) 상태로 변경합니다. 파손 이유를 추가할 수 있습니다."
    )
    public ApiResponse<Void> markEquipmentsAsBroken(
            @RequestParam List<Long> equipmentIds,
            @RequestParam(required = false) String description
    ) {
        adminEquipmentService.markEquipmentsAsBroken(equipmentIds, description);
        return ApiResponse.success("장비 고장/파손 처리 성공", null);
    }

    // 장비 복구 처리
    @PostMapping("/repair")
    @Operation(
            summary = "장비 복구 처리",
            description = "고장/파손(BROKEN) 상태의 장비들을 복구하여 대여 가능(AVAILABLE) 상태로 변경합니다. 복구 내역을 추가할 수 있습니다."
    )
    public ApiResponse<Void> repairEquipments(
            @RequestParam List<Long> equipmentIds,
            @RequestParam(required = false) String repairNote
    ) {
        adminEquipmentService.repairEquipments(equipmentIds, repairNote);
        return ApiResponse.success("장비 복구 처리 성공", null);
    }

    // 대여 기간 연장
    @PostMapping("/extend")
    @Operation(
            summary = "대여 기간 연장",
            description = "대여 중인 장비들의 반납 기한을 연장합니다."
    )
    public ApiResponse<Void> extendRentalPeriods(
            @RequestParam List<Long> equipmentIds,
            @RequestParam LocalDateTime newEndDate
    ) {
        adminEquipmentService.extendRentalPeriods(equipmentIds, newEndDate);
        return ApiResponse.success("대여 기간 연장 성공", null);
    }

    // 강제 회수
    @PostMapping("/force-return")
    @Operation(
            summary = "강제 회수",
            description = "대여 중인 장비들을 강제로 회수하여 대여 가능(AVAILABLE) 상태로 변경합니다."
    )
    public ApiResponse<Void> forceReturnEquipments(@RequestBody List<Long> equipmentIds) {
        adminEquipmentService.forceReturnEquipments(equipmentIds);
        return ApiResponse.success("강제 회수 성공", null);
    }


}
