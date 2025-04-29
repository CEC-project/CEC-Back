package com.backend.server.api.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.dto.classroom.AdminClassRoomCreateRequest;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomIdResponse;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomRentalListResponse;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomRentalRequestListRequest;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomRentalRequestListResponse;
import com.backend.server.api.admin.service.AdminClassRoomService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListRequest;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListResponse;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/classrooms")
@RequiredArgsConstructor
public class AdminClassRoomController {
    private final AdminClassRoomService adminClassRoomService;


    //강의실 등록
    @PostMapping
    public ApiResponse<AdminClassRoomIdResponse> createClassRoom(@Valid @RequestBody AdminClassRoomCreateRequest request) {
        adminClassRoomService.createClassRoom(request);
        return ApiResponse.success("success", null);
    }

    //강의실 업데이트
    @PutMapping("/{id}")
    public ApiResponse<AdminClassRoomIdResponse> updateClassRoom(@PathVariable Long id, AdminClassRoomCreateRequest request) {
        adminClassRoomService.updateClassRoom(id, request);
        return ApiResponse.success("success", null);
    }

    //강의실 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<AdminClassRoomIdResponse> deleteClassRoom(@PathVariable Long id) {
        adminClassRoomService.deleteClassRoom(id);
        return ApiResponse.success("success", null);
    }

    //강의실 조회
    @GetMapping("/{id}")
    public ApiResponse<CommonClassRoomResponse> getClassRoom(@PathVariable Long id) {
        return ApiResponse.success("success", adminClassRoomService.getClassRoom(id));
    }

    //강의실 목록 조회
    @GetMapping
    public ApiResponse<CommonClassRoomListResponse> getClassRoomList(CommonClassRoomListRequest request) {
        return ApiResponse.success("success", adminClassRoomService.getClassRooms(request));
    }

    //강의실 대여 / 반납 요청 조회
    @GetMapping("/rentals")
    public ApiResponse<AdminClassRoomRentalRequestListResponse> getRentalRequests(AdminClassRoomRentalRequestListRequest request) {
        return ApiResponse.success("success", adminClassRoomService.getRentalRequests(request));
    }
    
    //강의실 대여 요청 승인
    @PostMapping("/rentals/{rentalId}/approve")
    public ApiResponse<Void> approveRentalRequest(@PathVariable Long rentalId) {
        adminClassRoomService.approveRentalRequest(rentalId);
        return ApiResponse.success("success", null);
    }

    //강의실 대여 요청 반려
    @PostMapping("/rentals/{rentalId}/reject")
    public ApiResponse<Void> rejectRentalRequest(@PathVariable Long rentalId) {
        adminClassRoomService.rejectRentalRequest(rentalId);
        return ApiResponse.success("success", null);
    }

    //강의실 반납 요청 승인(정상반납)
    @PostMapping("/rentals/{rentalId}/return/approve")
    public ApiResponse<Void> approveReturnRequest(@PathVariable Long rentalId) {
        adminClassRoomService.approveReturnRequest(rentalId);
        return ApiResponse.success("success", null);
    }

    //강의실 반납 요청 승인(파손반납)
    @PostMapping("/rentals/{rentalId}/return/approve-damaged")
    public ApiResponse<Void> approveReturnRequestDamaged(@PathVariable Long rentalId) {
        adminClassRoomService.approveReturnRequestDamaged(rentalId);
        return ApiResponse.success("success", null);
    }
}

