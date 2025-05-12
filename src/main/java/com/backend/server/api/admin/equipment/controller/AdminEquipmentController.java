package com.backend.server.api.admin.equipment.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.equipment.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentIdResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentIdsResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;

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
    @Operation(
        summary = "장비 등록",
        description = "새로운 장비를 등록합니다. 이미지, 카테고리, 모델, 수량, 관리자, 설명, 제한 학년 등 정보를 입력할 수 있습니다."
    )
    public ApiResponse<AdminEquipmentIdsResponse> createEquipment(
        @RequestBody(description = "장비 등록 요청 DTO", required = true)
        AdminEquipmentCreateRequest request
    ) {
        return ApiResponse.success("장비 등록 성공", adminEquipmentService.createEquipment(request));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "장비 정보 수정",
        description = "기존 장비의 정보를 수정합니다. 이미지, 카테고리, 모델, 관리자, 설명, 제한 학년 등 정보를 변경할 수 있습니다."
    )
    public ApiResponse<AdminEquipmentIdResponse> updateEquipment(
        @PathVariable Long id,
        @RequestBody(description = "장비 수정 요청 DTO", required = true)
        AdminEquipmentCreateRequest request
    ) {
        return ApiResponse.success("장비 등록 성공", adminEquipmentService.updateEquipment(id, request));
    }

    //장비 삭제
    @DeleteMapping
    public ApiResponse<AdminEquipmentIdResponse> deleteEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 삭제 성공",adminEquipmentService.deleteEquipment(id));
    }

    //장비  리스트 어드민 조회
    @Operation(
        summary = "장비 리스트 어드민 조회",
        description = """
        다양한 검색, 정렬, 필터 조건으로 장비 리스트를 조회합니다.<br>
        <b>검색/필터 파라미터:</b><br>
        - categoryId: 장비 분류(카테고리) ID<br>
        - modelName: 모델명(부분 일치)<br>
        - serialNumber: 일련번호(부분 일치)<br>
        - status: 장비 상태(예: 정상, 고장 등)<br>
        - isAvailable: 대여 가능 여부(true/false)<br>
        - renterName: 현재 대여자 이름(부분 일치)<br>
        - searchKeyword: 모델명, 일련번호, 대여자 이름 통합 검색<br>
        <br>
        <b>정렬 파라미터:</b><br>
        - sortBy: 정렬 기준(id, createdAt, brokenCount, repairCount, rentalCount)<br>
        - sortDirection: 정렬 방향(asc, desc)<br>
        <br>
        <b>페이징 파라미터:</b><br>
        - page: 페이지 번호(0부터 시작)<br>
        - size: 한 페이지에 보여줄 개수(기본값 17)<br>
        """
    )
    @Parameters({
        @Parameter(name = "categoryId", description = "장비 분류(카테고리) ID"),
        @Parameter(name = "modelName", description = "모델명(부분 일치)"),
        @Parameter(name = "serialNumber", description = "일련번호(부분 일치)"),
        @Parameter(name = "status", description = "장비 상태(예: 정상, 고장 등)"),
        @Parameter(name = "isAvailable", description = "대여 가능 여부(true/false)"),
        @Parameter(name = "renterName", description = "현재 대여자 이름(부분 일치)"),
        @Parameter(name = "searchKeyword", description = "모델명, 일련번호, 대여자 이름 통합 검색"),
        @Parameter(name = "sortBy", description = "정렬 기준(id, createdAt, brokenCount, repairCount, rentalCount)"),
        @Parameter(name = "sortDirection", description = "정렬 방향(asc, desc)"),
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지에 보여줄 개수(기본값 17)")
    })
    @GetMapping("/list")
    public ApiResponse<AdminEquipmentListResponse> getEquipment(
        @ModelAttribute AdminEquipmentListRequest request
    ) {
        return ApiResponse.success("장비 리스트 조회 성공", adminEquipmentService.getEquipments(request));
    }

    //장비 단일 상세조회
    public ApiResponse<AdminEquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세조회 성공",adminEquipmentService.getEquipment(id));
    }

}
