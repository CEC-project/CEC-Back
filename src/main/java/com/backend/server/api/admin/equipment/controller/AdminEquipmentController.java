package com.backend.server.api.admin.equipment.controller;

import java.util.List;

import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            summary = "장비 목록 조회"
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

    @PostMapping("/status")
    @Operation(
            summary = "장비 상태 변경 (고장 또는 수리)",
            description = """
            지정된 장비들의 상태를 BROKEN 또는 REPAIR 처리합니다.
        
            - BROKEN: 장비를 고장 상태로 전환하고, 고장 내용을 기록합니다.
            - REPAIR: 고장 상태의 장비를 수리하여 AVAILABLE 상태로 전환하고, 수리 내용을 기록합니다.
            """
    )
    public ApiResponse<List<Long>> changeEquipmentStatus(
            @RequestBody AdminEquipmentBrokenOrRepairRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        List<Long> updatedIds = adminEquipmentService.changeStatus(request, loginUser);
        return ApiResponse.success("장비 상태 변경 성공", updatedIds);
    }


}
