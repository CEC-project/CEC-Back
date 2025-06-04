package com.backend.server.api.admin.rentalRestriction.controller;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListRequest;
import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListResponse;
import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionRequest;
import com.backend.server.api.admin.rentalRestriction.service.AdminRentalRestrictionService;
import com.backend.server.api.admin.user.dto.AdminUserListResponse;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/rental-restriction")
@RequiredArgsConstructor
@Tag(name = "2-1. 사용자 관리 / 제재 목록", description = "수정 필요")
public class AdminRentalRestrictionController {

    private final AdminRentalRestrictionService rentalRestrictionService;

    @Operation(summary = "제재 등록 API")
    @PostMapping("/user/{id}")
    public ApiResponse<Long> createRentalRestriction(
            @PathVariable("id") Long userId,
            @Valid @RequestBody AdminRentalRestrictionRequest request) {
        Long id = rentalRestrictionService.addRentalRestriction(userId, request);
        return ApiResponse.success("제재 등록 성공", id);
    }

    @Operation(summary = "제재 취소 API")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancelRentalRestriction(@PathVariable Long id) {
        rentalRestrictionService.cancelRentalRestriction(id);
        return ApiResponse.success("제재 취소 성공", null);
    }

    @Operation(summary = "현재 제재된 유저 목록 조회 API")
    @GetMapping("/restricted-user")
    public ApiResponse<AdminRentalRestrictionListResponse> getRestrictedUsers(
            @ParameterObject AdminRentalRestrictionListRequest request) {
        AdminRentalRestrictionListResponse result = rentalRestrictionService.getRestrictedUsers(request);
        return ApiResponse.success("현재 제재된 유저 목록 조회 성공", result);
    }

    @Operation(summary = "현재 제재되지 \"않은\" 유저 목록 조회 API")
    @GetMapping("/not-restricted-user")
    public ApiResponse<AdminUserListResponse> getAllowedUsers(
            @ParameterObject AdminRentalRestrictionListRequest request) {
        AdminUserListResponse result = rentalRestrictionService.getAllowedUsers(request);
        return ApiResponse.success("현재 제재되지 \"않은\" 유저 목록 조회 API", result);
    }
}