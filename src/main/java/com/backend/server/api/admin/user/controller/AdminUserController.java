package com.backend.server.api.admin.user.controller;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListResponse;
import com.backend.server.api.admin.user.dto.AdminUserRequest;
import com.backend.server.api.admin.user.dto.AdminUserResponse;
import com.backend.server.api.admin.user.service.AdminImportExcelService;
import com.backend.server.api.admin.user.service.AdminUserService;
import com.backend.server.api.common.dto.ApiResponse;

import com.backend.server.model.entity.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Tag(name = "2-1. 사용자 관리 / 사용자 목록", description = "수정 필요")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminImportExcelService excelService;

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "엑셀 파일로 사용자 정보 일괄 등록",
            description = "엑셀 파일을 업로드하여 사용자 정보를 일괄 등록합니다."
    )
    public ApiResponse<Integer> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success("사용자 정보가 성공적으로 등록되었습니다.", excelService.importUsersFromExcel(file));
    }

    @Operation(summary = "사용자 목록 조회")
    @GetMapping
    public ApiResponse<AdminUserListResponse> getUsers(@ParameterObject AdminUserListRequest request) {
        return ApiResponse.success("사용자 목록 조회 성공", adminUserService.getUsers(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ApiResponse.success("사용자 삭제 성공", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserRequest request) {
        return ApiResponse.success("사용자 수정 성공", adminUserService.updateUser(id, request));
    }

    @Operation(summary = "사용자 비밀번호 초기화 API",
            description = "비밀번호는 학번으로 초기화 됩니다.")
    @PatchMapping("/{id}")
    public ApiResponse<Long> updateUser(
            @PathVariable Long id) {
        return ApiResponse.success("비밀번호 초기화 성공", adminUserService.resetUserPassword(id));
    }

    @PostMapping
    public ApiResponse<Long> createUser(@Valid @RequestBody AdminUserRequest request) {
        return ApiResponse.success("사용자 등록 성공", adminUserService.createUser(request));
    }

    @Operation(
            summary = "관리자 목록 조회",
            description = """
    강의실 조회등에서 사용할 관리자 목록 조회기능
    """
    )
    @Parameters({
            @Parameter(
                    name = "roles",
                    description = "조회할 사용자 역할 (중복 선택 가능)<br>"
                            + "예: roles=ROLE_ADMIN&roles=ROLE_SUPER_ADMIN<br>"
                            + "생략할시 ROLE_SUPER_ADMIN 조회됨",
                    example = "ADMIN",
                    in = ParameterIn.QUERY,
                    array = @ArraySchema(
                            schema = @Schema(type = "string", allowableValues = {"ROLE_ADMIN", "ROLE_SUPER_ADMIN"}))
            )
    })
    @GetMapping("/admin")
    public ApiResponse<List<AdminUserResponse>> getAdmins(@RequestParam(required = false) List<Role> roles) {
        if (roles == null || roles.isEmpty())
            roles = Collections.singletonList(Role.ROLE_SUPER_ADMIN);
        if (roles.contains(Role.ROLE_USER))
            throw new IllegalArgumentException("유저 권한을 조회하는 API 가 아닙니다.");
        return ApiResponse.success("관리자 목록 조회 성공", adminUserService.getAdmins(roles));
    }
}