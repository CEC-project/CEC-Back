package com.backend.server.api.admin.yearSchedule.controller;

import com.backend.server.api.admin.yearSchedule.dto.AdminYearScheduleRequest;
import com.backend.server.api.admin.yearSchedule.dto.AdminYearScheduleResponse;
import com.backend.server.api.admin.yearSchedule.dto.AdminYearScheduleSearchRequest;
import com.backend.server.api.admin.yearSchedule.service.AdminYearScheduleService;
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
@RequestMapping("/api/admin/year-schedule")
@RequiredArgsConstructor
@Tag(name = "연간 일정 관리 API", description = "연간 일정 관리 어드민 API")
public class AdminYearScheduleController {

    private final AdminYearScheduleService adminYearScheduleService;

    @Operation(summary = "연간 일정 목록 조회 API")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<List<AdminYearScheduleResponse>> getYearSchedule(AdminYearScheduleSearchRequest request) {
        List<AdminYearScheduleResponse> result = adminYearScheduleService.getYearSchedules(request);
        return ApiResponse.success("연간 일정 목록 조회 성공", result);
    }

    @Operation(summary = "연간 일정 등록 API")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> createYearSchedule(@Valid @RequestBody AdminYearScheduleRequest request) {
        Long id = adminYearScheduleService.createYearSchedule(request);
        return ApiResponse.success("연간 일정 등록 성공", id);
    }

    @Operation(summary = "연간 일정 수정 API")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> updateYearSchedule(@PathVariable Long id, @Valid @RequestBody AdminYearScheduleRequest request) {
        Long updatedId = adminYearScheduleService.updateYearSchedule(id, request);
        return ApiResponse.success("연간 일정 수정 성공", updatedId);
    }

    @Operation(summary = "연간 일정 삭제 API")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Void> deleteYearSchedule(@PathVariable Long id) {
        adminYearScheduleService.deleteYearSchedule(id);
        return ApiResponse.success("연간 일정 삭제 성공", null);
    }
}