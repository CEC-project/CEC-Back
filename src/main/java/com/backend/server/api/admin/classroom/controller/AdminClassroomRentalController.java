package com.backend.server.api.admin.classroom.controller;

import com.backend.server.api.admin.classroom.dto.AdminClassroomRentalStatusRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.admin.classroom.service.AdminClassroomRentalService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/classroom-rental")
@RequiredArgsConstructor
@Tag(name = "4-1. 대여 관리 / 강의실 관리", description = "수정 필요")
public class AdminClassroomRentalController {

    private final AdminClassroomRentalService rentalService;

    @Operation(summary = "강의실 목록 조회 API")
    @GetMapping("")
    public ApiResponse<List<AdminClassroomDetailResponse>> getReturnableClassrooms(
            @ParameterObject AdminClassroomSearchRequest request) {
        List<AdminClassroomDetailResponse> result = rentalService.getClassrooms(request);
        return ApiResponse.success("강의실 목록 조회 성공", result);
    }

    @Operation(summary = "강의실 일괄 상태 변경 API")
    @PatchMapping("/status")
    public ApiResponse<List<Long>> changeStatus(@Valid @RequestBody AdminClassroomRentalStatusRequest request) {
        List<Long> ids = rentalService.changeStatus(request);
        return ApiResponse.success("강의실 일괄 상태 변경되었습니다.", ids);
    }
}
