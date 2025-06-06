package com.backend.server.api.admin.classroom.controller;

import com.backend.server.api.admin.classroom.dto.AdminClassroomRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomStatusRequest;
import com.backend.server.api.admin.classroom.service.AdminClassroomService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/classroom")
@RequiredArgsConstructor
@Tag(name = "3-2. 강의실/장비 관리 / 강의실 관리", description = "수정 필요")
public class AdminClassroomController {

    private final AdminClassroomService adminClassroomService;

    @Operation(
            summary = "강의실 목록 조회 API",
            description = """
            **강의실 대여 관리의 목록 조회 API 와 응답 DTO가 다릅니다.**""")
    @GetMapping
    public ApiResponse<List<AdminClassroomResponse>> searchClassrooms(
            @ParameterObject @Valid AdminClassroomSearchRequest request) {
        List<AdminClassroomResponse> result = adminClassroomService.searchClassrooms(request);
        return ApiResponse.success("강의실 목록 조회 성공", result);
    }

    @Operation(summary = "강의실 등록 API")
    @PostMapping
    public ApiResponse<Long> createClassroom(@Valid @RequestBody AdminClassroomRequest request) {
        Long id = adminClassroomService.createClassroom(request);
        return ApiResponse.success("강의실 등록 성공", id);
    }

    @Operation(summary = "강의실 수정 API")
    @PutMapping("/{id}")
    public ApiResponse<Long> updateClassroom(@PathVariable Long id, @Valid @RequestBody AdminClassroomRequest request) {
        Long updatedId = adminClassroomService.updateClassroom(id, request);
        return ApiResponse.success("강의실 수정 성공", updatedId);
    }

    @Operation(summary = "강의실 삭제 API", description = "**대여 중인 강의실은 삭제할 수 없습니다.**")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClassroom(@PathVariable Long id) {
        adminClassroomService.deleteClassroom(id);
        return ApiResponse.success("강의실 삭제 성공", null);
    }

    @PatchMapping("/status")
    @Operation(
            summary = "강의실 상태 변경 (고장 또는 수리) BROKEN, REPAIR중 선택"
    )
    public ApiResponse<List<Long>> changeClassroomStatus(
            @RequestBody AdminClassroomStatusRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        List<Long> updatedIds = adminClassroomService.changeStatus(request, loginUser);
        return ApiResponse.success("강의실 상태 변경 성공", updatedIds);
    }
}