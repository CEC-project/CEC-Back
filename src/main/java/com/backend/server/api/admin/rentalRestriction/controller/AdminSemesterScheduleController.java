package com.backend.server.api.admin.rentalRestriction.controller;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestriction;
import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionResponse;
import com.backend.server.api.admin.rentalRestriction.service.AdminRentalRestrictionService;
import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListResponse;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/rental-restriction")
@RequiredArgsConstructor
@Tag(name = "제재 관리 API", description = "제재 관리 어드민 API")
public class AdminSemesterScheduleController {

    private final AdminRentalRestrictionService adminRentalRestrictionService;

    @Operation(
            summary = "제재자 목록 조회",
            description = """
            관리자 페이지에서 사용자 목록을 검색, 필터링, 정렬, 페이징 조건에 따라 조회합니다.
            """
    )
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호 (기본값: 0)"),
            @Parameter(name = "size", description = "페이지당 항목 수 (기본값: 10)"),
            @Parameter(name = "searchKeyword", description = "검색 키워드"),
            @Parameter(name = "searchType", description = "검색 유형"),
            @Parameter(name = "sortBy", description = "정렬 기준"),
            @Parameter(name = "sortDirection", description = "정렬 방향 (asc 또는 desc)"),
            @Parameter(name = "grade", description = "학년 필터 (1~4)"),
            @Parameter(name = "gender", description = "성별 필터 ('남' 또는 '여')")
    })
    @GetMapping
    public ApiResponse<AdminRestrictionListResponse> getUsers(@Parameter(hidden = true) AdminRestrictionListRequest request) {
        return ApiResponse.success("사용자 목록 조회 성공",
                adminRentalRestrictionService.getRentalRestrictedUsers(request));
    }

    @Operation(summary = "제재 등록 API")
    @PostMapping
    public ApiResponse<Long> createSemesterSchedule(
            @RequestParam("semester_id") Long semesterId,
            @RequestParam("classroom_id") Long classroomId,
            @Valid @RequestBody AdminRentalRestriction request) {
        Long id = adminRentalRestrictionService.addRentalRestriction(semesterId, classroomId, request);
        return ApiResponse.success("제재 등록 성공", id);
    }

    @Operation(summary = "제재 수정 API")
    @PutMapping("/{id}")
    public ApiResponse<Long> updateSemesterSchedule(
            @PathVariable Long id,
            @Valid @RequestBody AdminRentalRestriction request) {
        Long updatedId = adminRentalRestrictionService.updateSemesterSchedule(id, request);
        return ApiResponse.success("제재 수정 성공", updatedId);
    }

    @Operation(summary = "제재 삭제 API")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSemesterSchedule(@PathVariable Long id) {
        adminRentalRestrictionService.cancelRentalRestriction(id);
        return ApiResponse.success("제재 삭제 성공", null);
    }
}