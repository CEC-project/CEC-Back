package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentListRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentListResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalOrReturnApprovalRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListResponse;
import com.backend.server.api.admin.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment API", description = "장비 관리 API")
public class AdminEquipmentController {

    private final AdminEquipmentService adminEquipmentService;

    @GetMapping("/admin-users")
    @Operation(summary = "관리자 목록 조회", description = "등록 가능한 관리자 목록을 조회합니다")
    public ApiResponse<List<AdminManagerCandidatesResponse>> getAdminUsers() {
        List<AdminManagerCandidatesResponse> adminUsers = adminEquipmentService.getAdminUsers();
        return ApiResponse.success("관리자 목록 조회 성공", adminUsers);
    }

    @GetMapping
    @Operation(summary = "모든 장비 조회", description = "모든 장비 목록을 조회합니다")
    public ApiResponse<AdminEquipmentListResponse> getAllEquipments(@RequestBody AdminEquipmentListRequest request) {
        AdminEquipmentListResponse equipments = adminEquipmentService.getEquipments(request);
        return ApiResponse.success("모든 장비 조회 성공", equipments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "개별 장비 조회", description = "ID로 특정 장비를 조회합니다")
    public ApiResponse<AdminEquipmentResponse> getEquipmentById(@PathVariable Long id) {
        AdminEquipmentResponse equipment = adminEquipmentService.getEquipment(id);
        return ApiResponse.success("개별 장비 조회 성공", equipment);
    }

    @PostMapping
    @Operation(summary = "장비 등록", description = "새로운 장비를 등록합니다")
    public ApiResponse<AdminEquipmentResponse> registerEquipment(@Valid @RequestBody AdminEquipmentCreateRequest request) {
        adminEquipmentService.createEquipment(request);
        return ApiResponse.success("장비 등록 성공", null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "장비 수정", description = "ID로 특정 장비를 수정합니다")
    public ApiResponse<AdminEquipmentResponse> updateEquipment(@PathVariable Long id, @Valid @RequestBody AdminEquipmentCreateRequest request) {
        adminEquipmentService.updateEquipment(id, request);
        return ApiResponse.success("장비 수정 성공", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "장비 삭제", description = "ID로 특정 장비를 삭제합니다")
    public ApiResponse<Void> deleteEquipment(@PathVariable Long id) {
        adminEquipmentService.deleteEquipment(id);
        return ApiResponse.success("장비 삭제 성공", null);
    }

    @GetMapping("/rental-requests")
    @Operation(summary = "장비 대여 요청 / 반납 목록 조회", description = "모든 장비 대여 요청 목록을 조회합니다")
    public ApiResponse<AdminEquipmentRentalRequestListResponse> getRentalRequests(@RequestBody AdminEquipmentRentalRequestListRequest request) {
        AdminEquipmentRentalRequestListResponse rentalRequests = adminEquipmentService.getRentalRequests(request);
        return ApiResponse.success("장비 대여 요청 목록 조회 성공", rentalRequests);
    }

    // @GetMapping("/return-requests")
    // @Operation(summary = "장비 반납 요청 목록 조회", description = "모든 장비 반납 요청 목록을 조회합니다")
    // public ApiResponse<AdminReturnRequestListResponse> getReturnRequests() {
    //     AdminReturnRequestListResponse returnRequests = adminEquipmentService.getReturnRequests();
    //     return ApiResponse.success("장비 반납 요청 목록 조회 성공", returnRequests);
    // }

    @PostMapping("/rental-requests/approve")
    @Operation(summary = "장비 대여 요청 승인 (다중선택)", description = "여러 장비 대여 요청을 승인합니다")
    public ApiResponse<Void> approveRentalRequests(@Valid @RequestBody AdminEquipmentRentalOrReturnApprovalRequest request) {
        
        adminEquipmentService.approveReturnRequestsNormal(request.getIds());
        return ApiResponse.success("장비 대여 요청 승인 성공", null);
    }

    @PostMapping("/rental-requests/{id}/reject")
    @Operation(summary = "장비 대여 요청 거절 (다중선택)", description = "특정 장비 대여 요청을 거절합니다")
    public ApiResponse<Void> rejectRentalRequest(@Valid @RequestBody AdminEquipmentRentalOrReturnApprovalRequest request) {
        adminEquipmentService.rejectRentalRequests(request.getIds());
        return ApiResponse.success("장비 대여 요청 거절 성공", null);
    }

    @PostMapping("/return-requests/{id}/approve")
    @Operation(summary = "장비 반납 요청 승인 (정상)", description = "특정 장비 반납 요청을 정상 상태로 승인합니다")
    public ApiResponse<Void> approveReturnRequestNormal(@Valid @RequestBody AdminEquipmentRentalOrReturnApprovalRequest request) {
        adminEquipmentService.approveReturnRequestsNormal(request.getIds());
        return ApiResponse.success("장비 반납 요청 정상 승인 성공", null);
    }

    @PostMapping("/return-requests/{id}/approve-damaged")
    @Operation(summary = "장비 반납 요청 승인 (파손)", description = "특정 장비 반납 요청을 파손 상태로 승인합니다")
    public ApiResponse<Void> approveReturnDamegedRequestsNormal(@Valid @RequestBody AdminEquipmentRentalOrReturnApprovalRequest request) {
        adminEquipmentService.approveReturnDamegedRequestsNormal(request.getIds());
        return ApiResponse.success("장비 반납 요청 파손 승인 성공", null);
    }
} 