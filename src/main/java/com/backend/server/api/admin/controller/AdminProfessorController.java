package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.dto.professor.AdminProfessorRequest;
import com.backend.server.api.admin.dto.professor.AdminProfessorResponse;
import com.backend.server.api.admin.service.AdminProfessorService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/professor")
@RequiredArgsConstructor
@Tag(name = "[관리자] 교수 관리 API", description = "교수 관리 어드민 API")
public class AdminProfessorController {

    private final AdminProfessorService adminProfessorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<List<AdminProfessorResponse>> getProfessors() {
        return ApiResponse.success("교수 목록 조회 성공", adminProfessorService.getProfessorList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> postProfessor(@RequestBody AdminProfessorRequest request) {
        return ApiResponse.success("교수 등록 성공", adminProfessorService.createProfessor(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Long> putProfessor(
            @PathVariable Long id,
            @RequestBody AdminProfessorRequest request) {
        return ApiResponse.success("교수 수정 성공", adminProfessorService.updateProfessor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Void> deleteProfessor(
            @PathVariable Long id) {
        adminProfessorService.deleteProfessor(id);
        return ApiResponse.success("교수 삭제 성공", null);
    }
}