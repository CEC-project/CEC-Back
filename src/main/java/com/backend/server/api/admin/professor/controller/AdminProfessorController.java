package com.backend.server.api.admin.professor.controller;

import com.backend.server.api.admin.professor.service.AdminProfessorService;
import com.backend.server.api.admin.professor.dto.AdminProfessorRequest;
import com.backend.server.api.admin.professor.dto.AdminProfessorResponse;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "1-1. 카테고리 관리 / 교수 관리", description = "작업 완료")
public class AdminProfessorController {

    private final AdminProfessorService adminProfessorService;

    @GetMapping
    public ApiResponse<List<AdminProfessorResponse>> getProfessors() {
        return ApiResponse.success("교수 목록 조회 성공", adminProfessorService.getProfessorList());
    }

    @PostMapping
    public ApiResponse<Long> postProfessor(@RequestBody AdminProfessorRequest request) {
        return ApiResponse.success("교수 등록 성공", adminProfessorService.createProfessor(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> putProfessor(
            @PathVariable Long id,
            @RequestBody AdminProfessorRequest request) {
        return ApiResponse.success("교수 수정 성공", adminProfessorService.updateProfessor(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProfessor(
            @PathVariable Long id) {
        adminProfessorService.deleteProfessor(id);
        return ApiResponse.success("교수 삭제 성공", null);
    }
}