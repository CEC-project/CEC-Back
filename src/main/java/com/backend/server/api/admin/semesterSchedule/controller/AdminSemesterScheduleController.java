package com.backend.server.api.admin.semesterSchedule.controller;

import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleRequest;
import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleResponse;
import com.backend.server.api.admin.semesterSchedule.service.AdminSemesterScheduleService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/semester-schedule")
@RequiredArgsConstructor
@Tag(name = "4-4. 강의실/장비 관리 / 수업 시간표 관리 / 수업 시간표", description = "수정 필요")
public class AdminSemesterScheduleController {

    private final AdminSemesterScheduleService adminSemesterScheduleService;

    @Operation(summary = "수업 시간표 목록 조회 API")
    @GetMapping("/{semesterId}/{classroomId}")
    public ApiResponse<List<AdminSemesterScheduleResponse>> getSemesterSchedule(
            @PathVariable @Parameter(example = "1") Long semesterId,
            @PathVariable @Parameter(example = "1") Long classroomId
    ) {
        List<AdminSemesterScheduleResponse> result = adminSemesterScheduleService
                .getSemesterSchedules(semesterId, classroomId);
        return ApiResponse.success("수업 시간표 목록 조회 성공", result);
    }

    @Operation(summary = "수업 시간표 등록 API")
    @PostMapping("/{semesterId}/{classroomId}")
    public ApiResponse<Long> createSemesterSchedule(
            @PathVariable @Parameter(example = "1") Long semesterId,
            @PathVariable @Parameter(example = "1") Long classroomId,
            @Valid @RequestBody AdminSemesterScheduleRequest request) {
        Long id = adminSemesterScheduleService.createSemesterSchedule(semesterId, classroomId, request);
        return ApiResponse.success("수업 시간표 등록 성공", id);
    }

    @Operation(summary = "수업 시간표 수정 API")
    @PutMapping("/{id}")
    public ApiResponse<Long> updateSemesterSchedule(
            @PathVariable Long id,
            @Valid @RequestBody AdminSemesterScheduleRequest request) {
        Long updatedId = adminSemesterScheduleService.updateSemesterSchedule(id, request);
        return ApiResponse.success("수업 시간표 수정 성공", updatedId);
    }

    @Operation(summary = "수업 시간표 삭제 API")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSemesterSchedule(@PathVariable Long id) {
        adminSemesterScheduleService.deleteSemesterSchedule(id);
        return ApiResponse.success("수업 시간표 삭제 성공", null);
    }
}