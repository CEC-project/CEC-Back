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

    @Operation(summary = "사용자 페이지네이션 조회 API",
            description = "검색 유형(=searchType)은 다음과 같습니다.<br/>"
                    + "0:이름으로 검색 | 1:전화번호로 검색 | 2:학번으로 검색<br/><br/>"
                    + "정렬 기준(=sortBy)은 다음과 같습니다.<br/>"
                    + "0:이름순 정렬 | 1:학번순 정렬 | 2:제제 횟수순 정렬<br/><br/>"
                    + "정렬 방향(=sortDirection)은 다음과 같습니다.<br/>"
                    + "asc:오름차순 | desc:내림차순<br/>")
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