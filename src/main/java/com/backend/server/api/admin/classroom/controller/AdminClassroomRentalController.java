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
            summary = "강의실 대여 승인 시킬 목록 조회 API",
            description = """
            **사용자가 대여 요청 날린 시간 기준 정렬**
            
            - **요청 바디**
              - **keyword** : 검색 키워드 (nullable, 예: "컴퓨터실")
              - **type** : 검색 타입 (nullable, 아래 중 하나)
                - **ID** : 강의실 ID로 검색
                - **NAME** : 강의실 이름으로 검색
                - **DESCRIPTION** : 강의실 설명으로 검색
                - **ALL** : 모든 항목에 대해 검색""")
    @GetMapping("/acceptable")
    public ApiResponse<List<AdminClassroomDetailResponse>> getAcceptableClassrooms(
            @ParameterObject AdminClassroomSearchRequest request) {
        List<AdminClassroomDetailResponse> result = rentalService.getAcceptableClassrooms(request);
        return ApiResponse.success("강의실 대여 승인 시킬 목록 조회 성공", result);
    }

    @Operation(
            summary = "강의실 반납시킬 목록 조회 API",
            description = """
            **사용자가 대여 요청 날린 시간 기준 정렬**
            
            - **요청 바디**
              - **keyword** : 검색 키워드 (nullable, 예: "컴퓨터실")
              - **type** : 검색 타입 (nullable, 아래 중 하나)
                - **ID** : 강의실 ID로 검색
                - **NAME** : 강의실 이름으로 검색
                - **DESCRIPTION** : 강의실 설명으로 검색
                - **ALL** : 모든 항목에 대해 검색""")
    @GetMapping("/returnable")
    public ApiResponse<List<AdminClassroomDetailResponse>> getReturnableClassrooms(
            @ParameterObject AdminClassroomSearchRequest request) {
        List<AdminClassroomDetailResponse> result = rentalService.getReturnableClassrooms(request);
        return ApiResponse.success("강의실 반납시킬 목록 조회 성공", result);
    }

    @Operation(
            summary = "강의실 승인 취소시킬 목록 조회 API",
            description = """
            **사용자가 대여 요청 날린 시간 기준 정렬**
            **대여 시작시간이 아직 안된 것만 뜹니다.**
            
            - **요청 바디**
              - **keyword** : 검색 키워드 (nullable, 예: "컴퓨터실")
              - **type** : 검색 타입 (nullable, 아래 중 하나)
                - **ID** : 강의실 ID로 검색
                - **NAME** : 강의실 이름으로 검색
                - **DESCRIPTION** : 강의실 설명으로 검색
                - **ALL** : 모든 항목에 대해 검색""")
    @GetMapping("/cancelable")
    public ApiResponse<List<AdminClassroomDetailResponse>> getCancelableClassrooms(
            @ParameterObject AdminClassroomSearchRequest request) {
        List<AdminClassroomDetailResponse> result = rentalService.getCancelableClassrooms(request);
        return ApiResponse.success("강의실 승인 취소시킬 목록 조회 성공", result);
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
