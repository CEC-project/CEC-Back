package com.backend.server.api.admin.classRoom.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.classRoom.dto.AdminClassRoomCreateRequest;
import com.backend.server.api.admin.classRoom.dto.AdminClassRoomIdResponse;
import com.backend.server.api.admin.classRoom.dto.AdminClassRoomRentalRequestListRequest;
import com.backend.server.api.admin.classRoom.dto.AdminClassRoomRentalRequestListResponse;
import com.backend.server.api.admin.classRoom.service.AdminClassRoomService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.classRoom.dto.CommonClassRoomListRequest;
import com.backend.server.api.common.classRoom.dto.CommonClassRoomListResponse;
import com.backend.server.api.common.classRoom.dto.CommonClassRoomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/classrooms")
@RequiredArgsConstructor
@Tag(name = "ClassRoom Admin API", description = "강의실 관리 어드민 API")
public class AdminClassRoomController {
    private final AdminClassRoomService adminClassRoomService;


    //강의실 등록
    @PostMapping
    @Operation(summary = "강의실 등록", description = "새로운 강의실을 등록합니다")
    public ApiResponse<AdminClassRoomIdResponse> createClassRoom(@Valid @RequestBody AdminClassRoomCreateRequest request) {
        adminClassRoomService.createClassRoom(request);
        return ApiResponse.success("success", null);
    }

    //강의실 업데이트
    @PutMapping("/{id}")
    @Operation(summary = "강의실 업데이트", description = "ID로 특정 강의실을 업데이트합니다")
    public ApiResponse<AdminClassRoomIdResponse> updateClassRoom(@PathVariable Long id, AdminClassRoomCreateRequest request) {
        adminClassRoomService.updateClassRoom(id, request);
        return ApiResponse.success("success", null);
    }

    //강의실 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "강의실 삭제", description = "ID로 특정 강의실을 삭제합니다")
    public ApiResponse<AdminClassRoomIdResponse> deleteClassRoom(@PathVariable Long id) {
        adminClassRoomService.deleteClassRoom(id);
        return ApiResponse.success("success", null);
    }

    //강의실 조회
    @GetMapping("/{id}")
    @Operation(summary = "강의실 조회", description = "ID로 특정 강의실을 조회합니다")
    public ApiResponse<CommonClassRoomResponse> getClassRoom(@PathVariable Long id) {
        return ApiResponse.success("success", adminClassRoomService.getClassRoom(id));
    }

    //강의실 목록 조회
    @GetMapping
    @Operation(
    summary = "강의실 필터링 조회",
    description = "강의실의 상태, 대여 가능 여부, 검색어, 즐겨찾기 여부 등을 기반으로 강의실 목록을 필터링하여 조회합니다."
    )
    public ApiResponse<CommonClassRoomListResponse> getClassRoomList(CommonClassRoomListRequest request) {
        return ApiResponse.success("success", adminClassRoomService.getClassRooms(request));
    }

    //강의실 대여 / 반납 요청 조회
    @GetMapping("/rentals")
    @Operation(
    summary = "대여 요청 필터링 조회",
    description = "대여 요청을 상태, 검색어, 날짜 범위 등을 기준으로 필터링하여 조회합니다."
    )
    public ApiResponse<AdminClassRoomRentalRequestListResponse> getRentalRequests(AdminClassRoomRentalRequestListRequest request) {
        return ApiResponse.success("success", adminClassRoomService.getRentalRequests(request));
    }
    
    //강의실 대여 요청 승인
    @PostMapping("/rentals/{rentalId}/approve")
    @Operation(summary = "강의실 대여 요청 승인", description = "ID로 특정 강의실 대여 요청을 승인합니다")
    public ApiResponse<Void> approveRentalRequest(@PathVariable Long rentalId) {
        adminClassRoomService.approveRentalRequest(rentalId);
        return ApiResponse.success("success", null);
    }

    //강의실 대여 요청 반려
    @PostMapping("/rentals/{rentalId}/reject")
    @Operation(summary = "강의실 대여 요청 반려", description = "ID로 특정 강의실 대여 요청을 반려합니다")
    public ApiResponse<Void> rejectRentalRequest(@PathVariable Long rentalId) {
        adminClassRoomService.rejectRentalRequest(rentalId);
        return ApiResponse.success("success", null);
    }

    //강의실 반납 요청 승인(정상반납)
    @PostMapping("/rentals/{rentalId}/return/approve")
    @Operation(summary = "강의실 반납 요청 승인(정상반납)", description = "ID로 특정 강의실 반납 요청을 정상 반납합니다")
    public ApiResponse<Void> approveReturnRequest(@PathVariable Long rentalId) {
        adminClassRoomService.approveReturnRequest(rentalId);
        return ApiResponse.success("success", null);
    }

    //강의실 반납 요청 승인(파손반납)
    @PostMapping("/rentals/{rentalId}/return/approve-damaged")
    @Operation(summary = "강의실 반납 요청 승인(파손반납)", description = "ID로 특정 강의실 반납 요청을 파손 반납합니다")
    public ApiResponse<Void> approveReturnRequestDamaged(@PathVariable Long rentalId) {
        adminClassRoomService.approveReturnRequestDamaged(rentalId);
        return ApiResponse.success("success", null);
    }
}

