package com.backend.server.api.admin.classroom.controller;

import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.admin.classroom.service.AdminClassroomService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/classroom")
@RequiredArgsConstructor
@Tag(name = "강의실 관리 API", description = "강의실 관리 어드민 API")
public class AdminClassroomController {

    private final AdminClassroomService adminClassroomService;

    @Operation(summary = "강의실 검색 API", description = "검색 조건에 따라 강의실을 조회합니다.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<List<AdminClassroomResponse>> searchClassrooms(@Valid AdminClassroomSearchRequest request) {
        List<AdminClassroomResponse> result = adminClassroomService.searchClassrooms(request);
        return ApiResponse.success("강의실 목록 조회 성공", result);
    }

    @Operation(summary = "강의실 등록 API")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> createClassroom(@Valid @RequestBody AdminClassroomRequest request) {
        Long id = adminClassroomService.createClassroom(request);
        return ApiResponse.success("강의실 등록 성공", id);
    }

    @Operation(summary = "강의실 수정 API")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> updateClassroom(@PathVariable Long id, @Valid @RequestBody AdminClassroomRequest request) {
        Long updatedId = adminClassroomService.updateClassroom(id, request);
        return ApiResponse.success("강의실 수정 성공", updatedId);
    }

    @Operation(summary = "강의실 삭제 API", description = "대여 중인 강의실은 삭제할 수 없습니다.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Void> deleteClassroom(@PathVariable Long id) {
        adminClassroomService.deleteClassroom(id);
        return ApiResponse.success("강의실 삭제 성공", null);
    }

    @Operation(summary = "강의실 파손 등록 API", description = "강의실을 파손 상태로 표시합니다.")
    @PostMapping("/{id}/broken")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> markAsBroken(@PathVariable Long id, @Valid @RequestBody AdminClassroomDetailRequest request) {
        Long brokenId = adminClassroomService.markAsBroken(id, request);
        return ApiResponse.success("강의실 파손 등록 성공", brokenId);
    }

    @Operation(summary = "강의실 수리 완료 API", description = "파손된 강의실을 정상 상태로 되돌립니다.")
    @PostMapping("/{id}/repair")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> repairClassroom(@PathVariable Long id, @Valid @RequestBody AdminClassroomDetailRequest request) {
        Long repairId = adminClassroomService.repairClassroom(id, request);
        return ApiResponse.success("강의실 수리 완료", repairId);
    }
}