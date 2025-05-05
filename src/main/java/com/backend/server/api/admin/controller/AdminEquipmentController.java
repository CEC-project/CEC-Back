package com.backend.server.api.admin.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentIdResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentIdsResponse;
import com.backend.server.api.admin.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.model.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/equipments")
public class AdminEquipmentController {
    
    private final AdminEquipmentService adminEquipmentService;
    //어드민 유저 조회 
    @GetMapping("/admin-users")
    @Operation(summary = "관리자 목록 조회", description = "등록 가능한 관리자 목록을 조회합니다")
    public ApiResponse<List<AdminManagerCandidatesResponse>> getAdminUsers() {
        List<AdminManagerCandidatesResponse> adminUsers = adminEquipmentService.getAdminUsers();
        return ApiResponse.success("관리자 목록 조회 성공", adminUsers);
    }

    //장비 등록
    @PostMapping
    public ApiResponse<AdminEquipmentIdsResponse> createEquipment(@RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 등록 성공", adminEquipmentService.createEquipment(request));
    }

    //장비 수정
    @PutMapping("/{id}")
    public ApiResponse<AdminEquipmentIdResponse> updateEquipment(@PathVariable Long id, @RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 등록 성공",adminEquipmentService.updateEquipment(id, request));
    }

    //장비 삭제
    @DeleteMapping
    public ApiResponse<AdminEquipmentIdResponse> deleteEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 삭제 성공",adminEquipmentService.deleteEquipment(id));
    }
    
    
}
