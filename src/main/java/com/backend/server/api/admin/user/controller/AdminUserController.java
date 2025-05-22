package com.backend.server.api.admin.user.controller;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListResponse;
import com.backend.server.api.admin.user.dto.AdminUserRequest;
import com.backend.server.api.admin.user.dto.AdminUserResponse;
import com.backend.server.api.admin.user.service.AdminUserService;
import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Tag(name = "사용자 관리 API", description = "사용자 관리 어드민 API")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(
            summary = "어드민 - 사용자 리스트 조회",
            description = """
    관리자 페이지에서 사용자 목록을 검색, 필터링, 정렬, 페이징 조건에 따라 조회합니다.

    <b>🔍검색/필터 조건:</b><br>
    - <code>searchKeyword</code>: 검색 키워드 (아래 searchType 기준에 따라 검색)<br>
    - <code>searchType</code>: 검색 유형 (0: 이름, 1: 전화번호, 2: 학번)<br>
    - <code>grade</code>: 학년 필터 (1, 2, 3, 4 중 하나 또는 생략)<br>
    - <code>gender</code>: 성별 필터 ('남', '여' 중 하나 또는 생략)<br>
    - <code>professorId</code>: 지도 교수 ID (자연수 하나 또는 생략)<br>

    <b>🔃정렬 조건:</b><br>
    - <code>sortBy</code>: 정렬 기준 (0: 이름(기본값), 1: 학번, 2: 제재 횟수)<br>
    - <code>sortDirection</code>: 정렬 방향 (asc: 오름차순(기본값), desc: 내림차순)<br>

    <b>📄페이징 조건:</b><br>
    - <code>page</code>: 페이지 번호 (기본값: 0)<br>
    - <code>size</code>: 페이지당 항목 수 (기본값: 10)<br>

    ⚠️ <b>검색을 수행하려면 <code>searchType</code>과 <code>searchKeyword</code>가 모두 지정되어야 합니다.</b>
    """
    )
    @GetMapping
    public ApiResponse<AdminUserListResponse> getUsers(AdminUserListRequest request) {
        return ApiResponse.success("사용자 목록 조회 성공", adminUserService.getUsers(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ApiResponse.success("사용자 삭제 성공", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<AdminUserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserRequest request) {
        return ApiResponse.success("사용자 수정 성공", adminUserService.updateUser(id, request));
    }

    @Operation(summary = "사용자 비밀번호 초기화 API",
            description = "비밀번호는 학번으로 초기화 됩니다.")
    @PatchMapping("/{id}")
    public ApiResponse<AdminUserResponse> updateUser(
            @PathVariable Long id) {
        return ApiResponse.success("비밀번호 초기화 성공", adminUserService.resetUserPassword(id));
    }

    @PostMapping
    public ApiResponse<AdminUserResponse> createUser(@Valid @RequestBody AdminUserRequest request) {
        return ApiResponse.success("사용자 등록 성공", adminUserService.createUser(request));
    }

    @GetMapping("/admin")
    public ApiResponse<List<AdminUserResponse>> getAdmins() {
        return ApiResponse.success("관리자 목록 조회 성공", adminUserService.getAdmins());
    }
}