package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.dto.user.AdminUserListRequest;
import com.backend.server.api.admin.dto.user.AdminUserListResponse;
import com.backend.server.api.admin.service.AdminUserService;
import com.backend.server.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ApiResponse<AdminUserListResponse> getUsers(AdminUserListRequest request) {
        return ApiResponse.success("사용자 목록 조회 성공", adminUserService.getUsers(request));
    }
}