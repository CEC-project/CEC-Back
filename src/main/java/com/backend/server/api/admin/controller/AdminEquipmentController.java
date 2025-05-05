package com.backend.server.api.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentIdResponse;
import com.backend.server.api.admin.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.model.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin/equipments")
public class AdminEquipmentController {
    
    private final AdminEquipmentService adminEquipmentService;
    //어드민 유저 조회 
    @GetMapping("/admin-users")
    @Operation(summary = "관리자 목록 조회", description = "등록 가능한 관리자 목록을 조회합니다")
    public ApiResponse<List<AdminManagerCandidatesResponse>> getAdminUsers() {
        List<User> adminUsers = adminEquipmentService.getAdminUsers();
        return ApiResponse.success("관리자 목록 조회 성공", adminUsers.stream().map(AdminManagerCandidatesResponse::new).collect(Collectors.toList()));
    }

    //장비 등록
    @PostMapping
    public ApiResponse<AdminEquipmentIdResponse> createEquipment(@RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success(adminEquipmentService.createEquipment(request));
    }

    //장비 수정
    @PutMapping
    public ApiResponse<AdminEquipmentIdResponse> updateEquipment(@RequestBody AdminEquipmentUpdateRequest request) {
        return ApiResponse.success(adminEquipmentService.updateEquipment(request));
    }

    //장비 삭제
    @DeleteMapping
    public ApiResponse<AdminEquipmentIdResponse> deleteEquipment(@RequestBody AdminEquipmentDeleteRequest request) {
        return ApiResponse.success(adminEquipmentService.deleteEquipment(request));
    }
    
    
}
