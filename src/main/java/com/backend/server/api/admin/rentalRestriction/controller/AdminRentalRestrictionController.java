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
@Tag(name = "제재 관리 API", description = "제재 관리 어드민 API")
public class AdminRentalRestrictionController {

    private final AdminRentalRestrictionService rentalRestrictionService;

    @Operation(
            summary = "제재 등록 API",
            description = """
    대여 제한 등록 요청
    
    - type: 제한 종류 (필수) (EQUIPMENT, CLASSROOM)
    - reason: 제한 사유 (필수) (OVERDUE, DAMAGED, LOST)
    - duration: 제한 기간 (양수, 단위: 일)
    
    모든 필드는 필수이며, duration은 1 이상의 정수여야 합니다.
    """)
    @PostMapping("/{userId}")
    public ApiResponse<Long> createRentalRestriction(
            @PathVariable Long userId,
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

    @Operation(
            summary = "현재 제재된 유저 목록 조회 API",
            description = """
    검색 조건:
    - searchKeyword: 검색 키워드 (아래 searchType 기준에 따라 검색) (생략시 전체 검색)
    - searchType: 검색 유형 (0: 이름, 1: 전화번호, 2: 학번, 3: 닉네임, 4: 전체) (생략시 전체)
    - grade: 학년 (1~4 또는 미지정)
    - gender: 성별 ('남' 또는 '여')
    - type: 제한 종류 (EQUIPMENT, CLASSROOM)
    - reason: 제한 사유 (OVERDUE, DAMAGED, LOST)
    - professorId: 담당 교수 ID
    
    페이징 및 정렬:
    - page: 페이지 번호 (기본값 0)
    - size: 페이지당 항목 수 (기본값 10)
    - sortBy: 정렬 기준 (NAME, STUDENT_NUMBER, RESTRICTION_COUNT, RESTRICTION_START_TIME, RESTRICTION_END_TIME)
    - direction: 정렬 방향 (ASC 또는 DESC)
    """)
    @GetMapping("/restricted-user")
    public ApiResponse<AdminRentalRestrictionListResponse> getRestrictedUsers(
            @ParameterObject AdminRentalRestrictionListRequest request) {
        AdminRentalRestrictionListResponse result = rentalRestrictionService.getRestrictedUsers(request);
        return ApiResponse.success("현재 제재된 유저 목록 조회 성공", result);
    }

    @Operation(
            summary = "현재 제재되지 \"않은\" 유저 목록 조회 API",
            description = """
    검색 조건:
    - searchKeyword: 검색 키워드 (아래 searchType 기준에 따라 검색) (생략시 전체 검색)
    - searchType: 검색 유형 (0: 이름, 1: 전화번호, 2: 학번, 3: 닉네임, 4: 전체) (생략시 전체)
    - grade: 학년 (1~4 또는 미지정)
    - gender: 성별 ('남' 또는 '여')
    - type: 제한 종류 (EQUIPMENT, CLASSROOM)
    - reason: 제한 사유 (OVERDUE, DAMAGED, LOST)
    - professorId: 담당 교수 ID
    
    페이징 및 정렬:
    - page: 페이지 번호 (기본값 0)
    - size: 페이지당 항목 수 (기본값 10)
    - sortBy: 정렬 기준 (NAME, STUDENT_NUMBER, RESTRICTION_COUNT, RESTRICTION_START_TIME, RESTRICTION_END_TIME)
    - direction: 정렬 방향 (ASC 또는 DESC)
    """)
    @GetMapping("/allowed-user")
    public ApiResponse<AdminUserListResponse> getAllowedUsers(
            @ParameterObject AdminRentalRestrictionListRequest request) {
        AdminUserListResponse result = rentalRestrictionService.getAllowedUsers(request);
        return ApiResponse.success("현재 제재된 유저 목록 조회 성공", result);
    }
}