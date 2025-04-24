package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.AdminRentalApprovalRequest;
import com.backend.server.api.admin.dto.AdminReturnApprovalRequest;
import com.backend.server.api.admin.dto.AdminRentalResponse;
import com.backend.server.api.admin.service.AdminService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.model.entity.Equipment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "관리자 API")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/equipments")
    @Operation(summary = "모든 장비 조회", description = "모든 장비 목록을 조회합니다")
    public ResponseEntity<List<Equipment>> getAllEquipments() {
        List<Equipment> equipments = adminService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/equipments/{id}")
    @Operation(summary = "개별 장비 조회", description = "ID로 특정 장비를 조회합니다")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        return adminService.getEquipmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/equipments")
    @Operation(summary = "장비 등록", description = "새로운 장비를 등록합니다")
    public ResponseEntity<Equipment> registerEquipment(@Valid @RequestBody AdminEquipmentCreateRequest request) {
        Equipment savedEquipment = adminService.createEquipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
    }

    @PutMapping("/equipments/{id}")
    @Operation(summary = "장비 수정", description = "ID로 특정 장비를 수정합니다")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @Valid @RequestBody AdminEquipmentCreateRequest request) {
        Equipment updatedEquipment = adminService.updateEquipment(id, request);
        return ResponseEntity.ok(updatedEquipment);
    }

    @DeleteMapping("/equipments/{id}")
    @Operation(summary = "장비 삭제", description = "ID로 특정 장비를 삭제합니다")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        adminService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rental-requests")
    @Operation(summary = "장비 대여 요청 목록 조회", description = "모든 장비 대여 요청 목록을 조회합니다")
    public ResponseEntity<List<AdminRentalResponse>> getRentalRequests() {
        List<AdminRentalResponse> rentalRequests = adminService.getRentalRequests();
        return ResponseEntity.ok(rentalRequests);
    }

    @GetMapping("/return-requests")
    @Operation(summary = "장비 반납 요청 목록 조회", description = "모든 장비 반납 요청 목록을 조회합니다")
    public ResponseEntity<List<AdminRentalResponse>> getReturnRequests() {
        List<AdminRentalResponse> returnRequests = adminService.getReturnRequests();
        return ResponseEntity.ok(returnRequests);
    }

    @PostMapping("/rental-requests/{id}/approve")
    @Operation(summary = "장비 대여 요청 승인", description = "특정 장비 대여 요청을 승인합니다")
    public ResponseEntity<ApiResponse> approveRentalRequest(
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