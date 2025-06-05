package com.backend.server.api.admin.equipment.controller;

import java.util.List;

import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "3-1. 강의실/장비 관리 / 장비 관리 / 장비", description = "수정 완료")
public class AdminEquipmentController {

    private final AdminEquipmentService adminEquipmentService;

    @GetMapping("/managers")
    @Operation(summary = "관리자 목록 조회", description = "장비 등록 또는 관리 권한을 가진 관리자 계정 목록을 조회합니다.")
    public ApiResponse<List<AdminManagerCandidatesResponse>> getAdminUsers() {
        return ApiResponse.success("관리자 목록 조회 성공", adminEquipmentService.getAdminUsers());
    }

    @PostMapping
    @Operation(
            summary = "장비 등록"

    )
    public ApiResponse<List<Long>> createEquipment(
            @RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 등록 성공", adminEquipmentService.createEquipment(request));
    }

    @GetMapping("/get-serial_number")
    @Operation(
            summary = "시리얼 넘버 미리보기 생성"
    )
    public ApiResponse<String> getSerialNumber(@ParameterObject AdminEquipmentSerialNumberGenerateRequest request) {
        return ApiResponse.success("시리얼넘버 보여주기 성공", adminEquipmentService.generateSerialNumber(request));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "장비 정보 수정"
    )
    public ApiResponse<Long> updateEquipment(
            @PathVariable Long id,
            @RequestBody AdminEquipmentCreateRequest request) {
        return ApiResponse.success("장비 수정 성공", adminEquipmentService.updateEquipment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "장비 삭제"
    )
    public ApiResponse<Long> deleteEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 삭제 성공", adminEquipmentService.deleteEquipment(id));
    }

    @GetMapping
    @Operation(
            summary = "장비 목록 조회"
    )
    public ApiResponse<AdminEquipmentListResponse> getEquipments(
            @ParameterObject AdminEquipmentListRequest request) {
        return ApiResponse.success("장비 리스트 조회 성공", adminEquipmentService.getEquipments(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 단일 조회"
    )
    public ApiResponse<AdminEquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세조회 성공", adminEquipmentService.getEquipment(id));
    }

//    @PutMapping("/{id}/status")
//    @Operation(
//            summary = "장비 상태 변경"
//    )
//    public ApiResponse<Long> updateEquipmentStatus(
//            @PathVariable Long id,
//            @RequestBody AdminEquipmentStatusUpdateRequest request) {
//        adminEquipmentService.updateEquipmentStatus(id, request);
//        return ApiResponse.success("장비 상태 변경 성공", id);
//    }

    @PatchMapping("/status")
    @Operation(
            summary = "장비 상태 변경 (고장 또는 수리) BROKEN, REPAIR중 선택"
    )
    public ApiResponse<List<Long>> changeEquipmentStatus(
            @RequestBody AdminEquipmentBrokenOrRepairRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        List<Long> updatedIds = adminEquipmentService.changeStatus(request, loginUser);
        return ApiResponse.success("장비 상태 변경 성공", updatedIds);
    }


}
