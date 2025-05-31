package com.backend.server.api.admin.classroom.controller;

import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailRequest;
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
@Tag(name = "강의실 대여 관리 API", description = "강의실 대여 관리 어드민 API")
public class AdminClassroomRentalController {

    private final AdminClassroomRentalService rentalService;

    @Operation(
            summary = "강의실 목록 조회 API",
            description = """
            - **요청 파라미터**
              - **keyword** : 검색 키워드 (nullable)
              - **type** : 검색 타입 (nullable)
                - **ALL** : 모든 항목에 대해 검색 (기본값)
                - **ID** : 강의실 ID로 검색
                - **NAME** : 강의실 이름으로 검색
                - **DESCRIPTION** : 강의실 설명으로 검색
              - **status** : 상태 필터 (nullable)
                - **ALL** : 모든 상태에 대해 검색 (기본값)
                - **AVAILABLE** : 대여 가능 상태
                - **IN_USE** : 대여 승인된 상태
                - **CANCELABLE** : 취소 가능 상태 (IN_USE 상태중에 아직 대여 시작시간이 되지 않은것)
                - **BROKEN** : 파손 상태
                - **RENTAL_PENDING** : 대여 신청 상태
              - **sortBy** : 정렬 기준 (nullable)
                - **status** : 강의실 상태 (기본값)
                - **requestedTime** : 대여 신청한 시각
                - **name** : 강의실 이름
                - **id** : 강의실 id
                - **description** : 강의실 설명
              - **sortDir** : 정렬 순서 (nullable)
                - **ASC** : 오름차순 (기본값)
                - **DESC** : 내림차순""")
    @GetMapping("/")
    public ApiResponse<List<AdminClassroomDetailResponse>> getReturnableClassrooms(
            @ParameterObject AdminClassroomSearchRequest request) {
        List<AdminClassroomDetailResponse> result = rentalService.getClassrooms(request);
        return ApiResponse.success("강의실 반납시킬 목록 조회 성공", result);
    }

    @Operation(
            summary = "강의실 일괄 상태 변경 API",
            description = """
            - **요청 바디**
              - **classroomIds** : 필수, 숫자 배열
              - **status** : 필수, RETURN, REJECT, CANCEL, BROKEN, ACCEPT
              - **detail** : 생략 가능, 100자 이내""")
    @PatchMapping("/status")
    public ApiResponse<List<Long>> changeStatus(@Valid @RequestBody AdminClassroomDetailRequest request) {
        List<Long> ids = rentalService.changeStatus(request);
        return ApiResponse.success("강의실 일괄 상태 변경되었습니다.", ids);
    }
}
