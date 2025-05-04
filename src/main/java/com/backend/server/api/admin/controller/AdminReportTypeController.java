package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.dto.category.AdminCommonCategoryRequest;
import com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse;
import com.backend.server.api.admin.service.AdminReportTypeService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/admin/report-type")
@RequiredArgsConstructor
@Tag(name = "신고 카테고리", description = "신고 카테고리 관리 어드민 API")
public class AdminReportTypeController {

    private final AdminReportTypeService adminReportTypeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<List<AdminCommonCategoryResponse>> getReportTypes() {
        return ApiResponse.success("신고 카테고리 목록 조회 성공", adminReportTypeService.getReportTypeList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> postReportType(@RequestBody AdminCommonCategoryRequest request) {
        return ApiResponse.success("신고 카테고리 등록 성공", adminReportTypeService.createReportType(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> putReportType(
            @PathVariable Long id,
            @RequestBody AdminCommonCategoryRequest request) {
        return ApiResponse.success("신고 카테고리 수정 성공", adminReportTypeService.updateReportType(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Void> deleteReportType(
            @PathVariable Long id) {
        adminReportTypeService.deleteReportType(id);
        return ApiResponse.success("신고 카테고리 삭제 성공", null);
    }
}