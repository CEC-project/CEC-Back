package com.backend.server.api.common.controller;

import com.backend.server.api.admin.service.AdminCategoryService;
import com.backend.server.api.common.dto.CategoryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "카테고리 조회 API")
public class CategoryController {

    private final AdminCategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회합니다.")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        // 관리자용 DTO를 일반 사용자용 DTO로 변환
        List<CategoryResponse> categories = categoryService.getAllCategories().stream()
                .map(adminDto -> CategoryResponse.builder()
                        .id(adminDto.getId())
                        .name(adminDto.getName())
                        .build())
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(categories);
    }
} 