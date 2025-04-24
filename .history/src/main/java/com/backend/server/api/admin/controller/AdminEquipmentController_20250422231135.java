package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListResponse;
import com.backend.server.api.admin.dto.AdminEquipmentManagerResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment API", description = "장비 관리 API")
public class AdminEquipmentController {

    private final AdminEquipmentService adminEquipmentService;

    @GetMapping("/admin-users")
    @Operation(summary = "관리자 목록 조회", description = "등록 가능한 관리자 목록을 조회합니다")
    public ApiResponse<List<AdminEquipmentManagerResponse>> getAdminUsers() {
        List<User> adminUsers = adminEquipmentService.getAdminUsers();
        return ApiResponse.success("관리자 목록 조회 성공", adminUsers.stream().map(AdminEquipmentManagerResponse::new).collect(Collectors.toList()));
    }

    @GetMapping("/equipments")
    @Operation(summary = "모든 장비 조회", description = "모든 장비 목록을 조회합니다")
    public ApiResponse<AdminEquipmentListResponse> getAllEquipments() {
        AdminEquipmentListResponse equipments = adminEquipmentService.getAllEquipments();
        return ApiResponse.success("모든 장비 조회 성공", equipments);
    }

    @GetMapping("/equipments/{id}")
    @Operation(summary = "개별 장비 조회", description = "ID로 특정 장비를 조회합니다")
    public ApiResponse<AdminEquipmentResponse> getEquipmentById(@PathVariable Long id) {
        AdminEquipmentResponse equipment = adminEquipmentService.getEquipment(id);
        return ApiResponse.success("개별 장비 조회 성공", equipment);
    }

    @PostMapping("/equipments")
    @Operation(summary = "장비 등록", description = "새로운 장비를 등록합니다")
    public ApiResponse<AdminEquipmentResponse> registerEquipment(@Valid @RequestBody AdminEquipmentCreateRequest request) {
        AdminEquipmentResponse equipment = adminEquipmentService.createEquipment(request);
        return ApiResponse.success("장비 등록 성공", equipment);
    }

    @PutMapping("/equipments/{id}")
    @Operation(summary = "장비 수정", description = "ID로 특정 장비를 수정합니다")
    public ApiResponse<AdminEquipmentResponse> updateEquipment(@PathVariable Long id, @Valid @RequestBody AdminEquipmentCreateRequest request) {
        AdminEquipmentResponse equipment = adminEquipmentService.updateEquipment(id, request);
        return ApiResponse.success("장비 수정 성공", equipment);
    }

    @DeleteMapping("/equipments/{id}")
    @Operation(summary = "장비 삭제", description = "ID로 특정 장비를 삭제합니다")
    public ApiResponse<Void> deleteEquipment(@PathVariable Long id) {
        adminEquipmentService.deleteEquipment(id);
        return ApiResponse.success("장비 삭제 성공", null);
    }

    @GetMapping("/rental-requests")
    @Operation(summary = "장비 대여 요청 목록 조회", description = "모든 장비 대여 요청 목록을 조회합니다")
    public ApiResponse<AdminRentalRequestListResponse> getRentalRequests() {
        AdminRentalRequestListResponse rentalRequests = adminEquipmentService.getRentalRequests();
        return ApiResponse.success("장비 대여 요청 목록 조회 성공", rentalRequests);
    }

    @GetMapping("/return-requests")
    @Operation(summary = "장비 반납 요청 목록 조회", description = "모든 장비 반납 요청 목록을 조회합니다")
    public ApiResponse<AdminReturnRequestListResponse> getReturnRequests() {
        AdminReturnRequestListResponse returnRequests = adminEquipmentService.getReturnRequests();
        return ApiResponse.success("장비 반납 요청 목록 조회 성공", returnRequests);
    }

    @PostMapping("/rental-requests/{id}/approve")
    @Operation(summary = "장비 대여 요청 승인", description = "특정 장비 대여 요청을 승인합니다")
    public ApiResponse<Void> approveRentalRequest(
            @PathVariable Long id, 
            @Valid @RequestBody AdminRentalApprovalRequest request) {
        adminService.approveRentalRequest(id, request);
        return ResponseEntity.ok(new ApiResponse("대여 요청이 승인되었습니다"));
    }

    @PostMapping("/rental-requests/{id}/reject")
    @Operation(summary = "장비 대여 요청 거절", description = "특정 장비 대여 요청을 거절합니다")
    public ResponseEntity<ApiResponse> rejectRentalRequest(
            @PathVariable Long id, 
            @Valid @RequestBody AdminRentalApprovalRequest request) {
        adminService.rejectRentalRequest(id, request);
        return ResponseEntity.ok(new ApiResponse("대여 요청이 거절되었습니다"));
    }

    @PostMapping("/return-requests/{id}/approve")
    @Operation(summary = "장비 반납 요청 승인 (정상)", description = "특정 장비 반납 요청을 정상 상태로 승인합니다")
    public ResponseEntity<ApiResponse> approveReturnRequestNormal(
            @PathVariable Long id, 
            @Valid @RequestBody AdminReturnApprovalRequest request) {
        adminService.approveReturnRequest(id, request, false);
        return ResponseEntity.ok(new ApiResponse("반납 요청이 정상 처리되었습니다"));
    }

    @PostMapping("/return-requests/{id}/approve-damaged")
    @Operation(summary = "장비 반납 요청 승인 (파손)", description = "특정 장비 반납 요청을 파손 상태로 승인합니다")
    public ResponseEntity<ApiResponse> approveReturnRequestDamaged(
            @PathVariable Long id, 
            @Valid @RequestBody AdminReturnApprovalRequest request) {
        adminService.approveReturnRequest(id, request, true);
        return ResponseEntity.ok(new ApiResponse("반납 요청이 파손 처리되었습니다"));
    }
} 