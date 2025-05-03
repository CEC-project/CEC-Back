package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.dto.user.AdminUserListRequest;
import com.backend.server.api.admin.dto.user.AdminUserListResponse;
import com.backend.server.api.admin.dto.user.AdminUserRequest;
import com.backend.server.api.admin.dto.user.AdminUserResponse;
import com.backend.server.api.admin.service.AdminUserService;
import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Tag(name = "User Admin API", description = "유저 관리 어드민 API")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<AdminUserListResponse> getUsers(AdminUserListRequest request) {
        return ApiResponse.success("사용자 목록 조회 성공", adminUserService.getUsers(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ApiResponse.success("사용자 삭제 성공", null);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<AdminUserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody AdminUserRequest request) {
        return ApiResponse.success("사용자 수정 성공", adminUserService.updateUser(id, request));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<AdminUserResponse> createUser(@RequestBody AdminUserRequest request) {
        return ApiResponse.success("사용자 등록 성공", adminUserService.createUser(request));
    }
}