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
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "3-2. 강의실/장비 관리 / 강의실 관리", description = "수정 필요")
public class AdminClassroomController {

    private final AdminClassroomService adminClassroomService;

    @Operation(
            summary = "강의실 목록 조회 API",
            description = """
            **강의실 대여 관리의 목록 조회 API 와 응답 DTO가 다릅니다.**
            
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
              - **sortDirection** : 정렬 순서 (nullable)
                - **ASC** : 오름차순 (기본값)
                - **DESC** : 내림차순""")
    @GetMapping
    public ApiResponse<List<AdminClassroomResponse>> searchClassrooms(
            @ParameterObject @Valid AdminClassroomSearchRequest request) {
        List<AdminClassroomResponse> result = adminClassroomService.searchClassrooms(request);
        return ApiResponse.success("강의실 목록 조회 성공", result);
    }

    @Operation(
            summary = "강의실 등록 API",
            description = """
        <b>$.attachment : 길이제한 255입니다. 생략 가능합니다. 빈 문자열이 들어가면 에러가 나므로, 대신 null을 넣어주세요.</b>""")
    @PostMapping
    public ApiResponse<Long> createClassroom(@Valid @RequestBody AdminClassroomRequest request) {
        Long id = adminClassroomService.createClassroom(request);
        return ApiResponse.success("강의실 등록 성공", id);
    }

    @Operation(
            summary = "강의실 수정 API",
            description = """
        <b>$.attachment : 길이제한 255입니다. 생략 가능합니다. 빈 문자열이 들어가면 에러가 나므로, 대신 null을 넣어주세요.</b>""")
    @PutMapping("/{id}")
    public ApiResponse<Long> updateClassroom(@PathVariable Long id, @Valid @RequestBody AdminClassroomRequest request) {
        Long updatedId = adminClassroomService.updateClassroom(id, request);
        return ApiResponse.success("강의실 수정 성공", updatedId);
    }

    @Operation(summary = "강의실 삭제 API", description = "대여 중인 강의실은 삭제할 수 없습니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClassroom(@PathVariable Long id) {
        adminClassroomService.deleteClassroom(id);
        return ApiResponse.success("강의실 삭제 성공", null);
    }

    @Operation(summary = "강의실 파손 등록 API", description = "강의실을 파손 상태로 표시합니다.")
    @PostMapping("/{id}/broken")
    public ApiResponse<Long> markAsBroken(@PathVariable Long id, @Valid @RequestBody AdminClassroomDetailRequest request) {
        Long brokenId = adminClassroomService.markAsBroken(id, request);
        return ApiResponse.success("강의실 파손 등록 성공", brokenId);
    }

    @Operation(summary = "강의실 수리 완료 API", description = "파손된 강의실을 정상 상태로 되돌립니다.")
    @PostMapping("/{id}/repair")
    public ApiResponse<Long> repairClassroom(@PathVariable Long id, @Valid @RequestBody AdminClassroomDetailRequest request) {
        Long repairId = adminClassroomService.repairClassroom(id, request);
        return ApiResponse.success("강의실 수리 완료", repairId);
    }
}