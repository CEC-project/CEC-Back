package com.backend.server.api.admin.semester.controller;

import com.backend.server.api.admin.semester.dto.AdminSemesterRequest;
import com.backend.server.api.admin.semester.dto.AdminSemesterResponse;
import com.backend.server.api.admin.semester.service.AdminSemesterService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/admin/semester")
@RequiredArgsConstructor
@Tag(name = "3-4. 강의실/장비 관리 / 수업 시간표 관리 / 학기", description = "수정 필요")
public class AdminSemesterController {

    private final AdminSemesterService adminSemesterService;

    @Operation(summary = "학기 목록 조회 API")
    @GetMapping
    public ApiResponse<List<AdminSemesterResponse>> getSemester() {
        return ApiResponse.success("학기 목록 조회 성공", adminSemesterService.getSemesters());
    }

    @Operation(summary = "학기 등록 API")
    @PostMapping
    public ApiResponse<Long> createSemester(@Valid @RequestBody AdminSemesterRequest request) {
        Long id = adminSemesterService.createSemester(request);
        return ApiResponse.success("학기 등록 성공", id);
    }

    @Operation(summary = "학기 수정 API")
    @PutMapping("/{id}")
    public ApiResponse<Long> updateSemester(@PathVariable Long id, @Valid @RequestBody AdminSemesterRequest request) {
        Long updatedId = adminSemesterService.updateSemester(id, request);
        return ApiResponse.success("학기 수정 성공", updatedId);
    }

    @Operation(summary = "학기 삭제 API")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSemester(@PathVariable Long id) {
        adminSemesterService.deleteSemester(id);
        return ApiResponse.success("학기 삭제 성공", null);
    }
}